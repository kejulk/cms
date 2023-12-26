package com.tms.microservice.async;

import com.tms.microservice.MicroserviceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    MicroserviceConfig microserviceConfig;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send("my-topic", message);
    }

    /** Can be configured */

    public void publishTableSchema() {
        System.out.println("Microservice id : "+microserviceConfig.getMicroserviceId());
        kafkaTemplate.send("table-name", microserviceConfig.getMicroserviceId()+"Candidate:candidateId,name,address,createdOn,updatedOn");
    }

    public void archiveComplete(String tablename) {
        System.out.println("Microservice id : "+microserviceConfig.getMicroserviceId());
        kafkaTemplate.send("archival-status", microserviceConfig.getMicroserviceId()+","+tablename+","+"COMPLETE");
    }



}
