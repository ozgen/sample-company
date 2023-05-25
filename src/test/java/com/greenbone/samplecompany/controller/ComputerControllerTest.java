package com.greenbone.samplecompany.controller;

import com.greenbone.samplecompany.exception.ComputerAlreadyAssignedException;
import com.greenbone.samplecompany.exception.ComputerNotFoundException;
import com.greenbone.samplecompany.exception.InvalidAbbreviationException;
import com.greenbone.samplecompany.model.data.Computer;
import com.greenbone.samplecompany.service.computer.ComputerService;
import com.greenbone.samplecompany.util.Converter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ComputerControllerTest {

    @Mock
    private ComputerService computerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new ComputerController(computerService))
                .setControllerAdvice(GlobalExceptionController.class)
                .build();
    }

    @Test
    void listComputers_Success() throws Exception {
        // given
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        List<Computer> computers = Arrays.asList(computer);
        when(computerService.getComputers()).thenReturn(computers);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/computer/"))
                .andExpect(status().isOk()).andReturn();
        // then
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(Converter.mapToJson(computers), content);
        verify(computerService, times(1)).getComputers();
    }

    @Test
    void listComputers_EmptyList() throws Exception {
        // given
        List<Computer> computers = new ArrayList<>();
        when(computerService.getComputers()).thenReturn(computers);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/computer/"))
                .andExpect(status().isOk()).andReturn();
        // then
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(Converter.mapToJson(computers), content);
        verify(computerService, times(1)).getComputers();
    }

    @Test
    void createComputer_Success() throws Exception {
        // given
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        String content = Converter.mapToJson(computer);
        when(computerService.createComputer(Mockito.any(Computer.class))).thenReturn(computer);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/computer/").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk()).andReturn();
        // then
        String actualContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(Converter.mapToJson(computer), actualContent);
        verify(computerService, times(1)).createComputer(Mockito.any());
    }

    @Test
    void createComputer_FailedWithInvalidAbbreviationException() throws Exception {
        // given
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        computer.setEmployeeAbbreviation("abcd");
        String content = Converter.mapToJson(computer);
        when(computerService.createComputer(Mockito.any(Computer.class))).thenThrow(new InvalidAbbreviationException("test"));

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/computer/").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is4xxClientError()).andReturn();
        // then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }


    @Test
    void getComputer_Success() throws Exception {
        // given
        Computer computer = new Computer();
        String id = UUID.randomUUID().toString();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        computer.setId(id);
        when(computerService.getComputer(Mockito.any(String.class))).thenReturn(computer);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        // then
        String actualContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(Converter.mapToJson(computer), actualContent);
        verify(computerService, times(1)).getComputer(Mockito.any());
    }

    @Test
    void getComputer_FailedWithComputerNotFoundException() throws Exception {
        // given
        Computer computer = new Computer();
        String id = UUID.randomUUID().toString();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        computer.setId(id);
        when(computerService.getComputer(Mockito.any(String.class))).thenThrow(new ComputerNotFoundException());

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()).andReturn();
        // then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }


    @Test
    void updateComputer_Success() throws Exception {
        // given
        Computer computer = new Computer();
        String id = UUID.randomUUID().toString();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        computer.setId(id);
        String content = Converter.mapToJson(computer);

        when(computerService.updateComputer(Mockito.any(String.class), Mockito.any(Computer.class))).thenReturn(computer);

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk()).andReturn();
        // then
        String actualContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(Converter.mapToJson(computer), actualContent);
        verify(computerService, times(1)).updateComputer(Mockito.any(), Mockito.any());
    }

    @Test
    void updateComputer_FailedWithComputerNotFoundException() throws Exception {
        // given
        Computer computer = new Computer();
        String id = UUID.randomUUID().toString();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        computer.setId(id);
        String content = Converter.mapToJson(computer);

        when(computerService.updateComputer(Mockito.any(String.class), Mockito.any(Computer.class))).thenThrow(new ComputerNotFoundException());

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isNotFound()).andReturn();
        // then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    void updateComputer_FailedWithInvalidAbbreviationException() throws Exception {
        // given
        Computer computer = new Computer();
        String id = UUID.randomUUID().toString();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        computer.setId(id);
        String content = Converter.mapToJson(computer);
        when(computerService.updateComputer(Mockito.any(String.class), Mockito.any(Computer.class))).thenThrow(new InvalidAbbreviationException("test"));

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is4xxClientError()).andReturn();
        // then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    void deleteComputer_Success() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        doNothing().when(computerService).deleteComputer(Mockito.any(String.class));

        // when and then
        MvcResult mvcResult = mockMvc.perform(delete("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    void deleteComputer_FailedWithComputerNotFoundException() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        doThrow(new ComputerNotFoundException()).when(computerService).deleteComputer(Mockito.any(String.class));

        // when
        MvcResult mvcResult = mockMvc.perform(delete("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()).andReturn();
        // then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    void assignComputer_Success() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        String employeeAbbreviation = "abc";
        doNothing().when(computerService).assignComputer(id, employeeAbbreviation);

        // when and then
         mockMvc.perform(patch("/api/computer/{id}/{abbreviation}", id, employeeAbbreviation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    void assignComputer_FailedWithComputerNotFoundException() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        String employeeAbbreviation = "abc";
        doThrow(new ComputerNotFoundException()).when(computerService).assignComputer(id, employeeAbbreviation);

        // when and then
        mockMvc.perform(patch("/api/computer/{id}/{abbreviation}", id, employeeAbbreviation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    void assignComputer_FailedWithComputerAlreadyAssignedException() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        String employeeAbbreviation = "abc";
        doThrow(new ComputerAlreadyAssignedException("test")).when(computerService).assignComputer(id, employeeAbbreviation);

        // when and then
        mockMvc.perform(patch("/api/computer/{id}/{abbreviation}", id, employeeAbbreviation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError()).andReturn();

    }

    @Test
    void assignComputer_FailedWithInvalidAbbreviationException() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        String employeeAbbreviation = "abc";
        doThrow(new InvalidAbbreviationException("test")).when(computerService).assignComputer(id, employeeAbbreviation);

        // when and then
        mockMvc.perform(patch("/api/computer/{id}/{abbreviation}", id, employeeAbbreviation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError()).andReturn();

    }

    @Test
    void unassignComputer_Success() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        doNothing().when(computerService).unassignComputer(id);

        // when and then
        mockMvc.perform(patch("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    void unassignComputer_FailedWithComputerNotFoundException() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        doThrow(new ComputerNotFoundException()).when(computerService).unassignComputer(id);

        // when and then
        mockMvc.perform(patch("/api/computer/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    void listAssignedComputers_Success() throws Exception {
        // given
        String employeeAbbreviation = "abc";
        Computer computer = new Computer();
        computer.setComputerName("Test Computer");
        computer.setMacAddress("00:11:22:33:44:55");
        computer.setIpV4Address("127.0.0.1");
        List<Computer> computers = Arrays.asList(computer);
        when(computerService.getAssignedComputers(Mockito.any())).thenReturn(computers);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/computer/assign/{abbreviation}",employeeAbbreviation))
                .andExpect(status().is2xxSuccessful()).andReturn();
        // then
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(Converter.mapToJson(computers), content);
        verify(computerService, times(1)).getAssignedComputers(Mockito.eq(employeeAbbreviation));
    }

    @Test
    void listAssignedComputers_EmptyList() throws Exception {
        // given
        String employeeAbbreviation = "abc";
        List<Computer> computers = new ArrayList<>();
        when(computerService.getAssignedComputers(Mockito.any())).thenReturn(computers);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/computer/assign/{abbreviation}",employeeAbbreviation))
                .andExpect(status().is2xxSuccessful()).andReturn();
        // then
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(Converter.mapToJson(computers), content);
        verify(computerService, times(1)).getAssignedComputers(Mockito.eq(employeeAbbreviation));
    }

}
