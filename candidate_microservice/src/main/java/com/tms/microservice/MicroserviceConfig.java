package com.tms.microservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroserviceConfig {

    @Value("${microservice.id}")
    private String microserviceId;

    public String getMicroserviceId() {
        return microserviceId;
    }
}
