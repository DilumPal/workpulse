package com.workpulse.hrms.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime; 

@Data
public class AttendanceResponseDTO {
    private Integer logId; 
    private String employeeName;
    private LocalDate workDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime; 
    private String status;
}