package com.marinagaisina.application.controller;

import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController  {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping
    public ResponseEntity<List<EmployeeEntity>> findAllEmployee(){
        return new ResponseEntity<>(employeeService.findAllEmployee(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> findEmployeeById(@PathVariable("id") Long id) {
        EmployeeEntity employee = employeeService.findById(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EmployeeEntity> addEmployee(@Valid @RequestBody EmployeeEntity employee) {
        return new ResponseEntity<>(this.employeeService.addEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<EmployeeEntity> updateEmployee(@Valid @RequestBody EmployeeEntity employee) {
        EmployeeEntity updatingEmployee = this.employeeService.updateEmployee(employee);
        if (updatingEmployee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
