package com.marinagaisina.application.service.impl;

import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.exception.EmployeeNotFoundException;
import com.marinagaisina.application.repository.EmployeeRepository;
import com.marinagaisina.application.service.EmployeeService;
import com.marinagaisina.application.validator.AllEmployees;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeEntity> findAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public EmployeeEntity findById(Long id) {
        Optional<EmployeeEntity> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    @Validated(AllEmployees.class)
    @Override
    public EmployeeEntity addEmployee(@Valid EmployeeEntity employeeEntity) {
        return employeeRepository.save(employeeEntity);
    }

    @Validated(AllEmployees.class)
    @Override
    public EmployeeEntity updateEmployee(@Valid EmployeeEntity employee) {
        Optional<EmployeeEntity> existingEmployee = employeeRepository.findById(employee.getId());
        if (existingEmployee.isPresent()) {
            return employeeRepository.save(employee);
        } else {
            throw new EmployeeNotFoundException("Employee with ID " + employee.getId() + " not found.");
        }
    }

    @Override
    public void deleteEmployee(Long id) {
        Optional<EmployeeEntity> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            employeeRepository.deleteById(id);
        } else {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
    }
}
