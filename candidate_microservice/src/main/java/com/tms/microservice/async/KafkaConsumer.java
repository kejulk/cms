package com.tms.microservice.async;

import com.tms.microservice.MicroserviceConfig;
import com.tms.microservice.entity.CandidateArchive;
import com.tms.microservice.repository.CandidateArchiveRepository;
import com.tms.microservice.repository.CandidateRepository;
import com.tms.microservice.repository.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class KafkaConsumer {

    @Autowired
    CandidateRepository repository;

    @Autowired
    CandidateArchiveRepository candidateArchiveRepository;

    @Autowired
    MicroserviceConfig microserviceConfig;

    @Autowired
    private KafkaProducer kafkaProducer;



    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }


    @KafkaListener(topics = "archive-job", groupId = "my-group")
    public void listenArchiveMessage(String message) {
        String [] info = message.split(",");
        String microserviceId = info[0];
        String tableName = info[1];
        String olderThan = info[2];
        if(!microserviceId.equals(microserviceConfig.getMicroserviceId()))
        {
            return;
        }

        System.out.println("Received Message: " + tableName + " "+ olderThan );
        //
        Date currentDate = new Date();
        // Calculate date 3 days before the current date
        Date threeDaysAgo = subtractDays(currentDate, Integer.parseInt(olderThan));
        repository.markForDelete(threeDaysAgo);
        repository.copyRecordsOlderThan(threeDaysAgo);

        // Initiate archival
        initateArchival();
        repository.deleteByMarkedForDeleteIsTrue();
        kafkaProducer.archiveComplete(tableName);
    }
    private static Date subtractDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        return calendar.getTime();
    }

    private void initateArchival() {
           String fileName =  exportToCsv(candidateArchiveRepository.findAll());
           sendToArchiveServer( fileName);
    }

    private String sendToArchiveServer(String filePath) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "exported_data.csv");

        HttpEntity<String> requestEntity = new HttpEntity<>(filePath, headers);

        // Replace the URL with the actual destination server's URL
        String destinationUrl = "http://destination-server/api/csv/archive";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(destinationUrl, requestEntity, String.class);

        ResponseEntity<String> response  = ResponseEntity<>(responseEntity.getBody(), headers, org.springframework.http.HttpStatus.OK);
        return response.getBody();
    }

    private String exportToCsv(List<CandidateArchive> data) {
        try (FileWriter fileWriter = new FileWriter("/candidate_archive_1.csv"))
        {
            for(CandidateArchive can : data)
            {
                fileWriter.append(can.toString());  // Adjust this based on your entity toString() method
                fileWriter.append("\n");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error exporting data to CSV", e);
        }
    }

}
