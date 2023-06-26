package com.greenbone.samplecompany.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenbone.samplecompany.configuration.AppConfig;
import com.greenbone.samplecompany.service.notification.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AppConfig config;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testSendNotificationToAlarmService() throws JsonProcessingException {
        // given
        String employeeAbbreviation = "mmu";
        String sendUrl = "http://localhost:8080/api/notify";
        String expectedMessage = "Employee " + employeeAbbreviation + " has reached the maximum number of assigned computers.";
        String expectedRequestBody = "{\"level\":\"warning\",\"employeeAbbreviation\":\"mmu\",\"message\":\"" + expectedMessage + "\"}";
        String expectedResponse = "Notification sent successfully.";
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(expectedRequestBody);

        NotificationServiceImpl notificationService = new NotificationServiceImpl(restTemplate, new ObjectMapper());

        // Set up the mock RestTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(expectedRequestBody, headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                sendUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        )).thenReturn(responseEntity);

        // when
        notificationService.sendNotificationToAlarmService(employeeAbbreviation, "http://localhost:8080/api/notify");

        // then
        verify(restTemplate, times(1)).exchange(
                eq(sendUrl),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        );
    }
    @Test
    void sendNotificationToAlarmService_FailedWithJsonProcessingException() throws JsonProcessingException {
        // given
        String employeeAbbreviation = "emp";
        String sendUrl = "https://example.com/notification";

        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenThrow(new JsonProcessingException("") {});
        NotificationServiceImpl notificationService = new NotificationServiceImpl(restTemplate, objectMapper);


        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> {
            notificationService.sendNotificationToAlarmService(employeeAbbreviation, sendUrl);
        });
    }
}
