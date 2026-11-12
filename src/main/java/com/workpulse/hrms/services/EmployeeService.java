package com.workpulse.hrms.services;

import com.workpulse.hrms.entities.Employee;
import com.workpulse.hrms.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

}
