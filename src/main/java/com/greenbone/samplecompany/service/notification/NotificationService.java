package com.greenbone.samplecompany.service.notification;

public interface NotificationService {
    /**
     * Sends a post request to the Alarm Service of GreenBone
     *
     * @param employeeAbbreviation of the employee
     */
    public void sendNotificationToAlarmService(String employeeAbbreviation);
}
