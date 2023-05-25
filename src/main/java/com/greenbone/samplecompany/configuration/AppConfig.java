package com.greenbone.samplecompany.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${sample-company.computerAssignedMaxSize}")
    private int computerAssignedMaxSize;

    @Value("${sample-company.notificationSendUrl}")
    private String notificationSendUrl;


    public int getComputerAssignedMaxSize() {
        return computerAssignedMaxSize;
    }

    public String getNotificationSendUrl() {
        return notificationSendUrl;
    }
}
