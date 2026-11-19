package com.workpulse.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HrDashboardStatsDTO {
    private long totalEmployees;
    private long pendingLeaves;
    private long onLeaveToday;
}