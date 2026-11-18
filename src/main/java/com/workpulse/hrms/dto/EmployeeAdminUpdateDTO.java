package com.workpulse.hrms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EmployeeAdminUpdateDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Optional in payload; only applied by the service if the caller is an Admin
    @Pattern(regexp = "^(Admin|HR Manager|Employee)$", message = "Must be 'Admin', 'HR Manager', or 'Employee'")
    private String role;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters and spaces")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters and spaces")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Job role is required")
    @Size(max = 100, message = "Job role cannot exceed 100 characters")
    private String jobRole;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    private BigDecimal salary;

    @NotBlank(message = "Employment type is required")
    @Pattern(regexp = "^(Full time|Part time|Contractor|Intern)$", message = "Must be 'Full time', 'Part time', 'Contractor', or 'Intern'")
    private String empType;

    @NotNull(message = "Department ID is required")
    private Integer departmentId;

    @Pattern(regexp = "^(https?://.+)?$", message = "Profile photo must be a valid HTTP or HTTPS URL")
    private String profilePhotoUrl;
}