package com.greenbone.samplecompany.service.notification;

import com.greenbone.samplecompany.model.event.MaxAssignmentsReachedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MaxAssignmentsReachedEventListener implements ApplicationListener<MaxAssignmentsReachedEvent> {

    private final NotificationService notificationService;

    public MaxAssignmentsReachedEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onApplicationEvent(MaxAssignmentsReachedEvent event) {
        String employeeAbbreviation = event.getEmployeeAbbreviation();
        notificationService.sendNotificationToAlarmService(employeeAbbreviation);
    }
}
