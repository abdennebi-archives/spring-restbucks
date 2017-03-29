package org.springsource.restbucks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Restbucks {

    public static void main(String[] args) {
        SpringApplication.run(Restbucks.class, args);
    }

}
