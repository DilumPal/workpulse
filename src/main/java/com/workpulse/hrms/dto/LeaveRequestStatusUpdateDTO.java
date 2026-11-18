package com.workpulse.hrms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LeaveRequestStatusUpdateDTO {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(Approved|Rejected)$", message = "Status must be either 'Approved' or 'Rejected'")
    private String status;

    private String adminComment;
}