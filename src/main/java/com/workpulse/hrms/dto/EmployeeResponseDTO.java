package com.workpulse.hrms.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class EmployeeResponseDTO {
    private UUID employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String role; 
    private String phoneNumber;
    private String jobRole;
    private BigDecimal salary;
    private String empType;
    private String profilePhotoUrl;
    private String departmentName;
}