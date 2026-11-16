package com.workpulse.hrms.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import org.hibernate.annotations.ColumnTransformer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID employeeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters and spaces")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters and spaces")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @NotBlank(message = "Job role is required")
    @Size(max = 100, message = "Job role cannot exceed 100 characters")
    @Column(name = "job_role", length = 100)
    private String jobRole;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;

    @NotBlank(message = "Employment type is required")
    @Pattern(regexp = "^(Full time|Part time|Contractor|Intern)$", message = "Must be 'Full time', 'Part time', 'Contractor', or 'Intern'")
    @Column(name = "emp_type", columnDefinition = "employment_type")
    @ColumnTransformer(write = "?::employment_type")
    private String empType;

    //validated if present
    @Pattern(regexp = "^(https?://.+)?$", message = "Profile photo must be a valid HTTP or HTTPS URL")
    @Column(name = "profile_photo_url", columnDefinition = "TEXT")
    private String profilePhotoUrl;

    @NotNull(message = "Department assignment is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;
}