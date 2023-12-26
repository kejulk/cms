package com.tms.microservice;

import com.tms.microservice.controller.CandidateController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class CandidateService {

	private static Logger logger = Logger.getLogger(CandidateController.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(CandidateService.class, args);

	}

}
