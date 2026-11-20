package com.workpulse.hrms.dto;

import lombok.Data;

@Data
public class DepartmentResponseDTO {
    private Integer departmentId;
    private String name;
    private String description;
}