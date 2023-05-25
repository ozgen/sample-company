package com.greenbone.samplecompany.service;

import com.greenbone.samplecompany.configuration.AppConfig;
import com.greenbone.samplecompany.exception.ComputerAlreadyAssignedException;
import com.greenbone.samplecompany.exception.ComputerNotFoundException;
import com.greenbone.samplecompany.exception.InvalidAbbreviationException;
import com.greenbone.samplecompany.model.data.Computer;
import com.greenbone.samplecompany.model.event.MaxAssignmentsReachedEvent;
import com.greenbone.samplecompany.repository.ComputerRepository;
import com.greenbone.samplecompany.service.computer.ComputerService;
import com.greenbone.samplecompany.service.computer.impl.ComputerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ComputerServiceTest {

    @Mock
    private ComputerRepository repository;

    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private AppConfig config;

    private ComputerService computerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(config.getComputerAssignedMaxSize()).thenReturn(3);
        computerService = new ComputerServiceImpl(repository, publisher,config);

    }

    @Test
    void createComputer_Success() {
        // given
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");

        when(repository.save(computer)).thenReturn(computer);

        // when
        Computer savedComputer = computerService.createComputer(computer);

        // then
        verify(repository, times(1)).save(computer);
        assertEquals(computer, savedComputer);
    }

    @Test
    void createComputer_FailedWithInvalidAbbreviationException() {
        // given
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setEmployeeAbbreviation("abcde"); // Invalid abbreviation

        // when and then
        Assertions.assertThrows(InvalidAbbreviationException.class, () -> computerService.createComputer(computer));
        verify(repository, never()).save(any(Computer.class));
    }

    @Test
    void updateComputer_Success() {
        // given
        String computerId = UUID.randomUUID().toString();
        Computer existingComputer = new Computer();
        existingComputer.setId(computerId);
        existingComputer.setComputerName("Old Computer");
        existingComputer.setMacAddress("00:11:22:33:44:55");

        Computer updatedComputer = new Computer();
        updatedComputer.setComputerName("New Computer");
        updatedComputer.setMacAddress("11:22:33:44:55:66");

        when(repository.findById(computerId)).thenReturn(Optional.of(existingComputer));
        when(repository.save(any(Computer.class))).thenReturn(updatedComputer);

        // when
        Computer savedComputer = computerService.updateComputer(computerId, updatedComputer);

        // then
        verify(repository, times(1)).findById(computerId);
        verify(repository, times(1)).save(existingComputer);
        assertEquals(updatedComputer, savedComputer);
        assertEquals(updatedComputer.getComputerName(), existingComputer.getComputerName());
        assertEquals(updatedComputer.getMacAddress(), existingComputer.getMacAddress());
    }

    @Test
    void updateComputer_FailedWithComputerNotFoundException() {
        // given
        String computerId = UUID.randomUUID().toString();
        Computer updatedComputer = new Computer();
        updatedComputer.setComputerName("New Computer");
        updatedComputer.setMacAddress("11:22:33:44:55:66");

        when(repository.findById(computerId)).thenReturn(Optional.empty());

        // when and then
        Assertions.assertThrows(ComputerNotFoundException.class, () -> computerService.updateComputer(computerId, updatedComputer));
        verify(repository, never()).save(any(Computer.class));
    }

    @Test
    void getComputer_Success() {
        // given
        String computerId = UUID.randomUUID().toString();
        Computer expectedComputer = new Computer();
        expectedComputer.setId(computerId);
        expectedComputer.setComputerName("Test Computer");

        when(repository.findById(computerId)).thenReturn(Optional.of(expectedComputer));

        // when
        Computer actualComputer = computerService.getComputer(computerId);

        // then
        verify(repository, times(1)).findById(computerId);
        assertEquals(expectedComputer, actualComputer);
    }

    @Test
    void getComputer_FailedWithComputerNotFoundException() {
        // given
        String computerId = UUID.randomUUID().toString();

        when(repository.findById(computerId)).thenReturn(Optional.empty());

        // when and then
        Assertions.assertThrows(ComputerNotFoundException.class, () -> computerService.getComputer(computerId));
        verify(repository, times(1)).findById(computerId);
    }

    @Test
    void deleteComputer_Success() {
        // given
        String computerId = UUID.randomUUID().toString();
        Computer existingComputer = new Computer();
        existingComputer.setId(computerId);
        existingComputer.setComputerName("Test Computer");

        when(repository.findById(computerId)).thenReturn(Optional.of(existingComputer));

        // when
        computerService.deleteComputer(computerId);

        // then
        verify(repository, times(1)).findById(computerId);
        verify(repository, times(1)).delete(existingComputer);
    }

    @Test
    void deleteComputer_FailedWithComputerNotFoundException() {
        // given
        String computerId = UUID.randomUUID().toString();

        when(repository.findById(computerId)).thenReturn(Optional.empty());

        // when and then
        Assertions.assertThrows(ComputerNotFoundException.class, () -> computerService.deleteComputer(computerId));
        verify(repository, times(1)).findById(computerId);
        verify(repository, never()).delete(any(Computer.class));
    }

    @Test
    void getComputers_Success() {
        // given
        List<Computer> expectedComputers = new ArrayList<>();
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        expectedComputers.add(computer);

        when(repository.findAll()).thenReturn(expectedComputers);

        // when
        List<Computer> actualComputers = computerService.getComputers();

        // then
        verify(repository, times(1)).findAll();
        assertEquals(expectedComputers, actualComputers);
    }

    @Test
    void getComputers_EmptyList() {
        // given
        List<Computer> expectedComputers = new ArrayList<>();

        when(repository.findAll()).thenReturn(expectedComputers);

        // when
        List<Computer> actualComputers = computerService.getComputers();

        // then
        verify(repository, times(1)).findAll();
        assertEquals(expectedComputers, actualComputers);
    }


    @Test
    void assignComputer_ComputerAssigned_Success() {
        // given
        String computerId = UUID.randomUUID().toString();
        String employeeAbbreviation = "abc";
        Computer computer = new Computer();
        computer.setId(computerId);
        computer.setEmployeeAbbreviation(null);
        long count = 3L;

        when(repository.findById(computerId)).thenReturn(Optional.of(computer));
        when(repository.save(any(Computer.class))).thenReturn(computer);
        when(repository.countComputerByEmployeeAbbreviation(any(String.class))).thenReturn(count);

        // when
        computerService.assignComputer(computerId, employeeAbbreviation);

        // then
        ArgumentCaptor<Computer> captor = ArgumentCaptor.forClass(Computer.class);
        ArgumentCaptor<MaxAssignmentsReachedEvent> eventCaptor = ArgumentCaptor.forClass(MaxAssignmentsReachedEvent.class);
        verify(repository, times(1)).save(captor.capture());
        assertEquals(employeeAbbreviation, captor.getValue().getEmployeeAbbreviation());
        verify(publisher, times(1)).publishEvent(eventCaptor.capture());
        MaxAssignmentsReachedEvent publishedEvent = eventCaptor.getValue();
        assertEquals(employeeAbbreviation, publishedEvent.getEmployeeAbbreviation());
    }

    @Test
    void assignComputer_FailedWithComputerAlreadyAssignedException() {
        // given
        String computerId = UUID.randomUUID().toString();
        String employeeAbbreviation = "abc";
        long computerCount = 5L;
        Computer computer = new Computer();
        computer.setId(computerId);
        computer.setEmployeeAbbreviation("def"); // Already assigned

        when(repository.findById(computerId)).thenReturn(Optional.of(computer));

        // when and then
        Assertions.assertThrows(ComputerAlreadyAssignedException.class, () -> computerService.assignComputer(computerId, employeeAbbreviation));
        verify(repository, never()).save(any(Computer.class));
        verify(publisher, never()).publishEvent(any(MaxAssignmentsReachedEvent.class));
    }

    @Test
    void assignComputer_FailedWithInvalidAbbreviationException() {
        // given
        String computerId =UUID.randomUUID().toString();
        String employeeAbbreviation = "abcd"; // Invalid abbreviation
        Computer computer = new Computer();
        computer.setId(computerId);
        when(repository.findById(computerId)).thenReturn(Optional.of(computer));

        // when and then
        Assertions.assertThrows(InvalidAbbreviationException.class, () -> computerService.assignComputer(computerId, employeeAbbreviation));
        verify(repository, never()).save(any(Computer.class));
        verify(publisher, never()).publishEvent(any(MaxAssignmentsReachedEvent.class));
    }


    @Test
    void unassignComputer_Success() {
        // given
        String computerId = UUID.randomUUID().toString();
        String employeeAbbreviation = "abc";
        Computer computer = new Computer();
        computer.setId(computerId);
        computer.setEmployeeAbbreviation(employeeAbbreviation);

        when(repository.findById(computerId)).thenReturn(Optional.of(computer));
        when(repository.save(any(Computer.class))).thenReturn(computer);

        // when
        computerService.unassignComputer(computerId);

        // then
        ArgumentCaptor<Computer> captor = ArgumentCaptor.forClass(Computer.class);
        verify(repository, times(1)).save(captor.capture());
        assertEquals(null, captor.getValue().getEmployeeAbbreviation());
    }

    @Test
    void unassignComputer_FailedWithComputerNotFoundException() {
        // given
        String computerId = UUID.randomUUID().toString();

        when(repository.findById(computerId)).thenReturn(Optional.empty());

        // when and then
        Assertions.assertThrows(ComputerNotFoundException.class, () -> computerService.unassignComputer(computerId));
        verify(repository, times(1)).findById(computerId);
        verify(repository, never()).save(any(Computer.class));
    }

    @Test
    void getAssignedComputers_Success() {
        // given
        List<Computer> expectedComputers = new ArrayList<>();
        String employeeAbbreviation = "abc";
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        computer.setEmployeeAbbreviation(employeeAbbreviation);
        expectedComputers.add(computer);

        when(repository.findByEmployeeAbbreviation(employeeAbbreviation)).thenReturn(expectedComputers);

        // when
        List<Computer> actualComputers = computerService.getAssignedComputers(employeeAbbreviation);

        // then
        verify(repository, times(1)).findByEmployeeAbbreviation(employeeAbbreviation);
        assertEquals(expectedComputers, actualComputers);
    }

    @Test
    void getAssignedComputers_EmptyList() {
        // given
        List<Computer> expectedComputers = new ArrayList<>();
        String employeeAbbreviation = "abc";

        when(repository.findByEmployeeAbbreviation(employeeAbbreviation)).thenReturn(expectedComputers);

        // when
        List<Computer> actualComputers = computerService.getAssignedComputers(employeeAbbreviation);

        // then
        verify(repository, times(1)).findByEmployeeAbbreviation(employeeAbbreviation);
        assertEquals(expectedComputers, actualComputers);
    }


}
