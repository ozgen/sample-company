package com.greenbone.samplecompany.model.data;


import com.greenbone.samplecompany.util.ComputerUtils;
import com.greenbone.samplecompany.util.PatternName;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;



@Entity
public class Computer {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull(message = "Mac Address cannot be null")
    @Pattern(regexp = ComputerUtils.MAC_PATTERN,
            message = "Mac address should be valid as : 00:00:0a:00:00:aa")
    private String macAddress;
    @NotNull(message = "Computer name cannot be null")
    private String computerName;
    @NotNull(message = "IPV4 address cannot be null")
    @Pattern(regexp = ComputerUtils.IPV4_PATTERN,
            message = "IPv4 address should be valid as : 255.255.255.255")
    private String ipV4Address;
    private String employeeAbbreviation;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getIpV4Address() {
        return ipV4Address;
    }

    public void setIpV4Address(String ipV4Address) {
        this.ipV4Address = ipV4Address;
    }

    public String getEmployeeAbbreviation() {
        return employeeAbbreviation;
    }

    public void setEmployeeAbbreviation(String employeeAbbreviation) {
        this.employeeAbbreviation = employeeAbbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

