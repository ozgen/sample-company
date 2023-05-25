package com.greenbone.samplecompany.model.event;

public class NotificationRequest {

    private String level;
    private String employeeAbbreviation;
    private String message;

    /**
     * This Data is used for sending notification to the Alarm Service
     * @param level of alarm
     * @param employeeAbbreviation of employee
     * @param message
     */
    public NotificationRequest(String level, String employeeAbbreviation, String message) {
        this.level = level;
        this.employeeAbbreviation = employeeAbbreviation;
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public String getEmployeeAbbreviation() {
        return employeeAbbreviation;
    }

    public String getMessage() {
        return message;
    }
}
