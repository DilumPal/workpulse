package com.workpulse.hrms.controllers;

import com.workpulse.hrms.dto.EmployeeCreateDTO;
import com.workpulse.hrms.dto.EmployeeResponseDTO;
import com.workpulse.hrms.dto.EmployeeUpdateDTO;
import com.workpulse.hrms.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Handled by SecurityConfig: Only Admins and HR Managers can reach this POST method
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeCreateDTO createDTO) {
        EmployeeResponseDTO newEmployee = employeeService.createEmployee(createDTO);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    // Self-Service Update: Any authenticated user can reach this, but they can only edit their own data
    @PutMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> updateMyProfile(
            @Valid @RequestBody EmployeeUpdateDTO updateDTO, 
            Principal principal) {
        
        // principal.getName() automatically contains the email extracted from the JWT by your JwtAuthenticationFilter
        String loggedInEmail = principal.getName();
        
        EmployeeResponseDTO updatedEmployee = employeeService.updateOwnProfile(loggedInEmail, updateDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Available to all logged-in roles based on SecurityConfig
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}