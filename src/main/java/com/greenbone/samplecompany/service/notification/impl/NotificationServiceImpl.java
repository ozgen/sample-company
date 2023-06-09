package com.greenbone.samplecompany.service.notification.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenbone.samplecompany.model.event.NotificationRequest;
import com.greenbone.samplecompany.service.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationServiceImpl implements NotificationService {

    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NotificationServiceImpl() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    // this constructor only used for unit test.
    public NotificationServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendNotificationToAlarmService(String employeeAbbreviation, String sendUrl) {
        String message = "Employee " + employeeAbbreviation + " has reached the maximum number of assigned computers.";

        NotificationRequest notificationRequest = new NotificationRequest("warning", employeeAbbreviation, message);

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request body
        try {
            String requestBody = objectMapper.writeValueAsString(notificationRequest);
            // Create the HttpEntity with headers and body
            HttpEntity<String> requestEntity =
                    new HttpEntity<String>(requestBody, headers);
            // Send the POST request
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    sendUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Process the response
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                logger.info("Notification is send successfully, response : {}", responseBody);
            } else {
                logger.error("Request failed with status code: {}", responseEntity.getStatusCodeValue());
            }
        } catch (JsonProcessingException e) {
            logger.error("An error occurred while sending notification to alarm service, Error: {}", e);
            throw new RuntimeException(e);
        }
    }
}
