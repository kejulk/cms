package com.tms.microservice.controller;


import com.tms.microservice.async.KafkaProducer;
import com.tms.microservice.repository.CandidateRepository;
import com.tms.microservice.entity.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/archive")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private KafkaProducer kafkaProducer;


    private Logger logger = Logger.getLogger(CandidateController.class.getName());

    @PostMapping(value = "/",produces = "application/json")
    public Candidate saveStudent(@RequestBody Candidate student){
        logger.info("Archive Content");
        Candidate student1 = candidateRepository.save(student);
        kafkaProducer.sendMessage("Archival started : ");
        return student1;
    }

    @GetMapping(value = "/archive/",produces = "application/json")
    public List<Candidate> getStudents(){
        logger.info("Retrieving all students");
        return candidateRepository.findAll();
    }

    @GetMapping(value = "/archive/candidate/{id}",produces = "application/json")
    public Optional<Candidate> getStudent(@PathVariable("id") Integer id){
        logger.info("Retrieving student by id");
        return candidateRepository.findById(id);
    }

    @DeleteMapping("/archive/candidate/delete/{id}")
    public void deleteStudent(@PathVariable("id") Integer id){
        logger.info("Deleting student by id");
        candidateRepository.deleteById(id);
    }
}