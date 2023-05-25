package com.greenbone.samplecompany.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.greenbone.samplecompany.configuration.AppConfig;
import com.greenbone.samplecompany.service.notification.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AppConfig config;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotificationToAlarmService() throws JsonProcessingException {
        // given
        String employeeAbbreviation = "mmu";
        String expectedMessage = "Employee " + employeeAbbreviation + " has reached the maximum number of assigned computers.";
        String expectedRequestBody = "{\"level\":\"warning\",\"employeeAbbreviation\":\"mmu\",\"message\":\"" + expectedMessage + "\"}";
        String expectedResponse = "Notification sent successfully.";

        when(config.getNotificationSendUrl()).thenReturn("http://localhost:8080/api/notify");
        NotificationServiceImpl notificationService = new NotificationServiceImpl(restTemplate, config);

        // Set up the mock RestTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(expectedRequestBody, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                "http://localhost:8080/api/notify",
                HttpMethod.POST,
                requestEntity,
                String.class
        )).thenReturn(responseEntity);

        // when
        notificationService.sendNotificationToAlarmService(employeeAbbreviation);

        // then
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/api/notify"),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        );
    }
}
