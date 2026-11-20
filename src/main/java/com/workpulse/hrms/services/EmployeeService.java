package com.workpulse.hrms.services;

import com.workpulse.hrms.dto.EmployeeAdminUpdateDTO;
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
import java.util.UUID;
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
        User user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new EntityNotFoundException("User account not found"));
                
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found for this user"));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getProfilePhotoUrl() != null) {
            employee.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        }

        employee = employeeRepository.save(employee);
        return convertToResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeAdminUpdateDTO dto, boolean isAdmin) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setJobRole(dto.getJobRole());
        employee.setSalary(dto.getSalary());
        employee.setEmpType(dto.getEmpType());
        employee.setDepartment(department);

        if (dto.getProfilePhotoUrl() != null) {
            employee.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        }

        if (employee.getUser() != null) {
            User user = employee.getUser();
            user.setEmail(dto.getEmail());

            // Only Admins can change the access level
            if (isAdmin && dto.getRole() != null && !dto.getRole().isBlank()) {
                user.setRole(dto.getRole());
            }
            userRepository.save(user);
        }

        employee = employeeRepository.save(employee);
        return convertToResponseDTO(employee);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId, String loggedInEmail) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (employee.getUser() != null &&
                employee.getUser().getEmail().equalsIgnoreCase(loggedInEmail)) {
            throw new IllegalStateException("You cannot delete your own profile.");
        }

        User user = employee.getUser();
        employeeRepository.delete(employee);

        if (user != null) {
            userRepository.delete(user);
        }
    }

    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User account not found"));

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found for this user"));

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
        
        if (employee.getUser() != null) {
            dto.setEmail(employee.getUser().getEmail());
            dto.setRole(employee.getUser().getRole());
        }
        
        if (employee.getDepartment() != null) {
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        
        return dto;
    }
}