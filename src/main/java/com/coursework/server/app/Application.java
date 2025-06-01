package com.coursework.server.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.coursework.server.app.controller",
    "com.coursework.server.app.service",
    "com.coursework.server.app.repository",
    "com.coursework.server.app.security"
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
