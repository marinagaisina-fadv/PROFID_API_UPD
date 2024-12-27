package com.marinagaisina.application.service;

import com.marinagaisina.application.entity.EmployeeEntity;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<EmployeeEntity> findAllEmployee();
    EmployeeEntity findById(Long id);

    EmployeeEntity addEmployee(@Valid EmployeeEntity employeeEntity);

    EmployeeEntity updateEmployee(@Valid EmployeeEntity employeeEntity);
    void deleteEmployee(Long id);

}
