package com.workpulse.hrms.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import org.hibernate.annotations.ColumnTransformer;

@Entity 
@Table(name = "employees")
@Data
public class Employee {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID employeeId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "job_role", length = 100)
    private String jobRole;

    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(name = "emp_type", columnDefinition = "employment_type")
    @ColumnTransformer(write = "?::employment_type") // This forces PostgreSQL to accept the string
    private String empType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;
}