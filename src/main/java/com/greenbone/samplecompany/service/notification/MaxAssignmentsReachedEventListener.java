package com.greenbone.samplecompany.service.notification;

import com.greenbone.samplecompany.configuration.AppConfig;
import com.greenbone.samplecompany.model.event.MaxAssignmentsReachedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MaxAssignmentsReachedEventListener implements ApplicationListener<MaxAssignmentsReachedEvent> {
    /**
     * MaxAssignmentsReachedEventListener is used for handling {@link MaxAssignmentsReachedEvent}.
     * It listens for the event and triggers the notification process.
     */
    private final NotificationService notificationService;
    private final String sendUrl;

    public MaxAssignmentsReachedEventListener(NotificationService notificationService, AppConfig config) {
        this.notificationService = notificationService;
        this.sendUrl = config.getNotificationSendUrl();
    }

    @Override
    public void onApplicationEvent(MaxAssignmentsReachedEvent event) {
        String employeeAbbreviation = event.getEmployeeAbbreviation();
        notificationService.sendNotificationToAlarmService(employeeAbbreviation, sendUrl);
    }
}
