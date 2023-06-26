package com.greenbone.samplecompany.service;
import com.greenbone.samplecompany.configuration.AppConfig;
import com.greenbone.samplecompany.exception.InvalidAbbreviationException;
import com.greenbone.samplecompany.model.event.MaxAssignmentsReachedEvent;
import com.greenbone.samplecompany.service.notification.MaxAssignmentsReachedEventListener;
import com.greenbone.samplecompany.service.notification.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class MaxAssignmentsReachedEventListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private AppConfig config;

    @Test
    void onApplicationEvent_shouldSendNotificationToAlarmService() {
        // given
        MockitoAnnotations.openMocks(this);

        String employeeAbbreviation = "emp";
        String sendUrl = "https://example.com/notification";
        Mockito.when(config.getNotificationSendUrl()).thenReturn(sendUrl);
        MaxAssignmentsReachedEvent event = new MaxAssignmentsReachedEvent(this, employeeAbbreviation);
        MaxAssignmentsReachedEventListener listener = new MaxAssignmentsReachedEventListener(notificationService, config);


        // when
        listener.onApplicationEvent(event);

        // then
        ArgumentCaptor<String> employeeAbbreviationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sendUrlCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(notificationService).sendNotificationToAlarmService(
                employeeAbbreviationCaptor.capture(),
                sendUrlCaptor.capture()
        );

        String capturedEmployeeAbbreviation = employeeAbbreviationCaptor.getValue();
        String capturedSendUrl = sendUrlCaptor.getValue();

        Assertions.assertEquals(employeeAbbreviation, capturedEmployeeAbbreviation);
        Assertions.assertEquals(sendUrl, capturedSendUrl);
    }
}

