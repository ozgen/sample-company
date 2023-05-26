package com.greenbone.samplecompany.service.computer.impl;

import com.greenbone.samplecompany.configuration.AppConfig;
import com.greenbone.samplecompany.exception.ComputerAlreadyAssignedException;
import com.greenbone.samplecompany.exception.ComputerNotFoundException;
import com.greenbone.samplecompany.exception.InvalidAbbreviationException;
import com.greenbone.samplecompany.model.data.Computer;
import com.greenbone.samplecompany.model.event.MaxAssignmentsReachedEvent;
import com.greenbone.samplecompany.repository.ComputerRepository;
import com.greenbone.samplecompany.service.computer.ComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ComputerServiceImpl implements ComputerService {

    private final ComputerRepository repository;

    private final ApplicationEventPublisher publisher;

    private final int computerAssignedMaxSize;

    private Logger logger = LoggerFactory.getLogger(ComputerService.class);

    public ComputerServiceImpl(ComputerRepository repository, ApplicationEventPublisher publisher,
                               AppConfig config) {
        this.repository = repository;
        this.publisher = publisher;
        computerAssignedMaxSize = config.getComputerAssignedMaxSize();
    }

    @Override
    public Computer createComputer(Computer computer) {
        //set abbreviation null, abbreviation is only updated in assignComputer method.
        computer.setEmployeeAbbreviation(null);
        //save computer
        Computer savedComputer = repository.save(computer);
        logger.info("{} of the computer is saved successfully.", savedComputer.getComputerName());
        return savedComputer;
    }

    @Override
    public Computer updateComputer(String id, Computer computer) {

        //get existing computer from db
        Computer existing = repository.findById(id).orElseThrow(ComputerNotFoundException::new);
        //update existing computer
        existing.setMacAddress(computer.getMacAddress());
        existing.setComputerName(computer.getComputerName());
        existing.setIpV4Address(computer.getIpV4Address());
        existing.setEmployeeAbbreviation(null); // set abbreviation null
        existing.setDescription(computer.getDescription());
        //save computer
        Computer savedComputer = repository.save(existing);
        logger.info("Id : {} of the computer is updated successfully.", savedComputer.getId());
        return savedComputer;
    }

    @Override
    public Computer getComputer(String id) {
        return repository.findById(id).orElseThrow(ComputerNotFoundException::new);
    }

    @Override
    public void deleteComputer(String id) {
        // check the computer is existed
        Computer computer = repository.findById(id).orElseThrow(ComputerNotFoundException::new);
        repository.delete(computer);
        logger.info("computer is deleted successfully id: {}", id);
    }

    @Override
    public List<Computer> getComputers() {
        return repository.findAll();
    }

    @Override
    public void assignComputer(String computerId, String employeeAbbreviation) {
        // check the computer is existed
        Computer computer = repository.findById(computerId).orElseThrow(ComputerNotFoundException::new);
        if (computer.getEmployeeAbbreviation() != null) {

            logger.error("This computer was already assigned : {}", computer.getEmployeeAbbreviation());
            throw new ComputerAlreadyAssignedException("This computer was already assigned!");
        }
        // check employeeAbbreviation is valid
        if (employeeAbbreviation != null) {
            employeeAbbreviation = employeeAbbreviation.toLowerCase();
            if (employeeAbbreviation.length() != 3) {
                logger.error("Employee abbreviation is not valid  abbreviation : {}", employeeAbbreviation);
                throw new InvalidAbbreviationException("Employee abbreviation should consists of 3 letters");
            } else {
                computer.setEmployeeAbbreviation(employeeAbbreviation);
                //save computer
                repository.save(computer);
                logger.info("Computer is assigned to employee : {} successfully", employeeAbbreviation);
                notifySystemAdmin(employeeAbbreviation);
            }
        }
    }

    @Override
    public void unassignComputer(String id) {
        // check the computer is existed
        Computer computer = repository.findById(id).orElseThrow(ComputerNotFoundException::new);
        computer.setEmployeeAbbreviation(null); //set employeeAbbreviation null
        //save computer
        repository.save(computer);
        logger.info("Computer is unassigned successfully id: {}", id);

    }

    @Override
    public List<Computer> getAssignedComputers(String employeeAbbreviation) {
        return repository.findByEmployeeAbbreviation(employeeAbbreviation);
    }

    @Override
    public void notifySystemAdmin(String employeeAbbreviation) {
        Long count = repository.countComputerByEmployeeAbbreviation(employeeAbbreviation);
        // check the counts of computers related employeeAbbreviation
        if (count >= computerAssignedMaxSize) {
            MaxAssignmentsReachedEvent event = new MaxAssignmentsReachedEvent(this, employeeAbbreviation);
            // publish maxAssignmentsReachedEvent
            publisher.publishEvent(event);
            logger.info("MaxAssignmentsReachedEvent is published successfully employeeAbbreviation: {}", employeeAbbreviation);
        }
    }

}
