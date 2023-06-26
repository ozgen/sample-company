package com.greenbone.samplecompany.service.notification;

public interface NotificationService {
    /**
     * Sends a post request to the Alarm Service of GreenBone
     *
     * @param employeeAbbreviation of the employee
     * @param sendUrl of sending notification
     */
    public void sendNotificationToAlarmService(String employeeAbbreviation, String sendUrl);
}
