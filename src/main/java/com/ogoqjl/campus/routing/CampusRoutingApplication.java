package com.ogoqjl.campus.routing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CampusRoutingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CampusRoutingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Done");
    }
    }
