package com.greenbone.samplecompany.repository;

import com.greenbone.samplecompany.model.data.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, String> {
    /**
     * Returns the assigned computer of the employee
     *
     * @param employeeAbbreviation of the employee
     * @return List of computers {@link Computer}
     */
    List<Computer> findByEmployeeAbbreviation(String employeeAbbreviation);

    /**
     * Returns the counts of assigned computer of the employee
     *
     * @param employeeAbbreviation of the employee
     * @return counts of computers related abbreviation {@link Computer}
     */
    Long countComputerByEmployeeAbbreviation(String employeeAbbreviation);
}
