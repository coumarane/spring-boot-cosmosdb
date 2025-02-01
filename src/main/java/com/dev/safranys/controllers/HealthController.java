package com.dev.safranys.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/health")
public class HealthController {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot v1!";
    }
}
