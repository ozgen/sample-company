package com.greenbone.samplecompany.service.computer;

import com.greenbone.samplecompany.model.data.Computer;

import java.util.List;

public interface ComputerService {
    /**
     *Stores the given computer to the h2 database
     * @param computer object to be stored
     * @return Computer {@link Computer} persisted object
     */
    public Computer createComputer(Computer computer);

    /**
     *Updates the given computer to the h2 database
     * @param computer object to be updated
     * @param id of the computer
     * @return Computer {@link Computer} persisted object
     */
    public Computer updateComputer(String id, Computer computer);

    /**
     * Retrieves the computer to the h2 database with given id
     * @param id of the computer
     * @return Computer {@link Computer}
     */
    public Computer getComputer(String id);

    /**
     * Deletes the computer to the h2 database with given id
     * @param id of the computer
     */
    public void deleteComputer(String id);

    /**
     * Retrieves all computers from the h2 database
     *
     * @return List of computers {@link Computer}
     */
    public List<Computer> getComputers();

    /**
     * Assign the computer to the employee with given parameters
     * @param computerId of the computer
     * @param employeeAbbreviation of the employee
     */
    public void assignComputer(String computerId, String employeeAbbreviation);

    /**
     * Unassigned the computer from any user.
     * @param computerId of the computer
     */
    public void unassignComputer(String computerId);

    /**
     * Gets list of assigned computers of the employee
     * @param employeeAbbreviation of the employee
     * @return List of computers {@link Computer}
     */

    public List<Computer> getAssignedComputers(String employeeAbbreviation);

    /**
     * Notifies the system admin If needed
     * @param employeeAbbreviation of the employee
     */

    public void notifySystemAdmin(String employeeAbbreviation);
}
