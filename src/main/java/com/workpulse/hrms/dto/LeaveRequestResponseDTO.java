package com.workpulse.hrms.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LeaveRequestResponseDTO {
    private Integer leaveId;
    private UUID employeeId;
    private String employeeName;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal requestedDuration;
    private String reason;
    private String status;
    private String reviewedBy; // Name of the HR/Admin who approved it
    private LocalDateTime createdAt;
}