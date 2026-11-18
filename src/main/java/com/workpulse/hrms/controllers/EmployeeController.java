package com.workpulse.hrms.controllers;

import com.workpulse.hrms.dto.EmployeeAdminUpdateDTO;
import com.workpulse.hrms.dto.EmployeeCreateDTO;
import com.workpulse.hrms.dto.EmployeeResponseDTO;
import com.workpulse.hrms.dto.EmployeeUpdateDTO;
import com.workpulse.hrms.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // POST: Create Employee (Admin & HR Manager)
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeCreateDTO createDTO,
            Authentication authentication) {
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().toUpperCase().contains("ADMIN"));
        
        // Enforce that HR Managers can only create standard employees
        if (!isAdmin) {
            createDTO.setRole("Employee");
        }

        EmployeeResponseDTO newEmployee = employeeService.createEmployee(createDTO);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    // GET: Fetch logged-in user's profile
    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getMyProfile(Principal principal) {
        String loggedInEmail = principal.getName();
        EmployeeResponseDTO profile = employeeService.getEmployeeByEmail(loggedInEmail);
        return ResponseEntity.ok(profile);
    }

    // PUT: Self-Service Update (Accepts multipart/form-data for photo uploads)
    @PutMapping(value = "/me", consumes = "multipart/form-data")
    public ResponseEntity<EmployeeResponseDTO> updateMyProfile(
            @Valid @ModelAttribute EmployeeUpdateDTO updateDTO, 
            Principal principal) {
        
        String loggedInEmail = principal.getName();
        EmployeeResponseDTO updatedEmployee = employeeService.updateOwnProfile(loggedInEmail, updateDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    // GET: All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // PUT: Edit Employee (Admin edits everything; HR Manager cannot change access level)
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody EmployeeAdminUpdateDTO updateDTO,
            Authentication authentication) {
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().toUpperCase().contains("ADMIN"));

        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, updateDTO, isAdmin);
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE: Delete Employee (Admin & HR Manager)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}