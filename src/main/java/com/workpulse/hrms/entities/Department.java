package com.workpulse.hrms.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "departments")
@Data // Lombok automatically generates getters, setters, and constructors
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}