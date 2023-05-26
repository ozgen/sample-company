package com.greenbone.samplecompany.controller;

import com.greenbone.samplecompany.model.data.Computer;
import com.greenbone.samplecompany.service.computer.ComputerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/computer")
public class ComputerController {

    private final ComputerService service;

    public ComputerController(ComputerService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Computer> listComputers() {
        return service.getComputers();
    }

    @PostMapping("/")
    public Computer createComputer(@RequestBody @Valid Computer computer) {
        return service.createComputer(computer);
    }

    @GetMapping("/{id}")
    public Computer getComputer(@PathVariable String id) {
        return service.getComputer(id);
    }

    @PutMapping("/{id}")
    public Computer updateComputer(@PathVariable String id, @RequestBody @Valid Computer computer) {
        return service.updateComputer(id, computer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComputer(@PathVariable String id) {
        service.deleteComputer(id);
    }

    //https://stackoverflow.com/questions/24241893/should-i-use-patch-or-put-in-my-rest-api
    @PatchMapping("/{id}/{abbreviation}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignComputer(@PathVariable String id, @PathVariable String abbreviation) {
        service.assignComputer(id, abbreviation);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassignComputer(@PathVariable String id) {
        service.unassignComputer(id);
    }

    @GetMapping("/assign/{abbreviation}")
    public List<Computer> listAssignComputer(@PathVariable String abbreviation) {
        return service.getAssignedComputers(abbreviation);
    }
}
