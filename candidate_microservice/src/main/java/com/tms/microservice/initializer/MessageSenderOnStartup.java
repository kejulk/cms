package com.tms.microservice.initializer;

import com.tms.microservice.async.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderOnStartup implements CommandLineRunner {

    @Autowired
    KafkaProducer kafkaProducer;


    @Autowired
    public MessageSenderOnStartup(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void run(String... args) throws Exception {

        // Send the message
        kafkaProducer.publishTableSchema();

        System.out.println("Message sent on application startup: " + "schema");
    }
}
