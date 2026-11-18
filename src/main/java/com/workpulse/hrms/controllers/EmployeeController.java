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

    // POST: Create Employee
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeCreateDTO createDTO) {
        EmployeeResponseDTO newEmployee = employeeService.createEmployee(createDTO);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    // NEW GET: Fetch logged-in user's profile
    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getMyProfile(Principal principal) {
        String loggedInEmail = principal.getName();
        
        // Fetch the employee using your service based on their authenticated email
        // Note: Make sure 'getEmployeeByEmail' exists in your EmployeeService
        EmployeeResponseDTO profile = employeeService.getEmployeeByEmail(loggedInEmail);
        
        return ResponseEntity.ok(profile);
    }

    // UPDATED PUT: Self-Service Update (Accepts multipart/form-data for photo uploads)
    @PutMapping(value = "/me", consumes = "multipart/form-data")
    public ResponseEntity<EmployeeResponseDTO> updateMyProfile(
            @Valid @ModelAttribute EmployeeUpdateDTO updateDTO, 
            Principal principal) {
        
        // principal.getName() automatically contains the email extracted from the JWT
        String loggedInEmail = principal.getName();
        
        EmployeeResponseDTO updatedEmployee = employeeService.updateOwnProfile(loggedInEmail, updateDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    // GET: All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}