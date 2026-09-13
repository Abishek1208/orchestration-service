package com.dev.orchestration_service.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController
{

    private final RestTemplate restTemplate;

    @Value("${test-service.base-url}")
    private String testServiceBaseUrl;

    public TestController(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/ping")
    public String ping()
    {
        log.info("Received ping request");
        String response = "pong from v5.0";
        log.info("Returning response: {}", response);
        return response;
    }

    @GetMapping("/call-test-service")
    public String callTestService()
    {
        String url = testServiceBaseUrl + "/test/ping";
        log.info("Calling test-service at {}", url);
        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("Received response from test-service: {}", response);
            return response;
        }
        catch (Exception e) {
            log.warn("Error calling test-service: {}", e.getMessage());
            return "Error calling test-service: " + e.getMessage();
        }
    }
}
