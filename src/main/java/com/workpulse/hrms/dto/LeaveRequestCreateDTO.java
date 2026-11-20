package com.workpulse.hrms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LeaveRequestCreateDTO {

    @NotBlank(message = "Leave type is required")
    @Pattern(regexp = "^(Annual Leave|Sick Leave|Casual Leave)$", message = "Must be 'Annual Leave', 'Sick Leave', or 'Casual Leave'")
    private String leaveType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Requested duration is required")
    @Positive(message = "Duration must be greater than zero")
    private BigDecimal requestedDuration;

    private String reason;
}