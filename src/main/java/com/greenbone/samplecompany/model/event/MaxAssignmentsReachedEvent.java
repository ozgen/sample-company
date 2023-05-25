package com.greenbone.samplecompany.model.event;

import org.springframework.context.ApplicationEvent;

public class MaxAssignmentsReachedEvent extends ApplicationEvent {
    private final String employeeAbbreviation;

    public MaxAssignmentsReachedEvent(Object source, String employeeAbbreviation) {
        super(source);
        this.employeeAbbreviation = employeeAbbreviation;
    }

    public String getEmployeeAbbreviation() {
        return employeeAbbreviation;
    }
}
