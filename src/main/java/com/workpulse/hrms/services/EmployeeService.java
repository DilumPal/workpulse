package com.workpulse.hrms.services;

import com.workpulse.hrms.dto.EmployeeCreateDTO;
import com.workpulse.hrms.dto.EmployeeResponseDTO;
import com.workpulse.hrms.dto.EmployeeUpdateDTO;
import com.workpulse.hrms.entities.Department;
import com.workpulse.hrms.entities.Employee;
import com.workpulse.hrms.entities.User;
import com.workpulse.hrms.repositories.DepartmentRepository;
import com.workpulse.hrms.repositories.EmployeeRepository;
import com.workpulse.hrms.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository,
                           DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO dto) {
        // 1. Verify the department exists
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        // 2. Create and secure the User account
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user = userRepository.save(user);

        // 3. Create the Employee profile and link dependencies
        Employee employee = new Employee();
        employee.setUser(user);
        employee.setDepartment(department);
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setJobRole(dto.getJobRole());
        employee.setSalary(dto.getSalary());
        employee.setEmpType(dto.getEmpType());
        employee.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        
        employee = employeeRepository.save(employee);

        return convertToResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO updateOwnProfile(String loggedInEmail, EmployeeUpdateDTO dto) {
        // 1. Find the employee record linked to the logged-in JWT email
        User user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new EntityNotFoundException("User account not found"));
                
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found for this user"));

        // 2. Update only the allowed self-service fields (notice salary and role are physically missing here)
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getProfilePhotoUrl() != null) {
            employee.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        }

        employee = employeeRepository.save(employee);
        return convertToResponseDTO(employee);
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to flatten nested entities and hide the password hash
    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setJobRole(employee.getJobRole());
        dto.setSalary(employee.getSalary());
        dto.setEmpType(employee.getEmpType());
        dto.setProfilePhotoUrl(employee.getProfilePhotoUrl());
        
        // Safely extract User data only if a linked User account actually exists
        if (employee.getUser() != null) {
            dto.setEmail(employee.getUser().getEmail());
            dto.setRole(employee.getUser().getRole());
        }
        
        // Safely extract Department data only if a linked Department actually exists
        if (employee.getDepartment() != null) {
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        
        return dto;
    }
}