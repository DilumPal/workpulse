package com.workpulse.hrms.controllers;

import com.workpulse.hrms.dto.HrDashboardStatsDTO;
import com.workpulse.hrms.repositories.EmployeeRepository;
import com.workpulse.hrms.repositories.LeaveRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public DashboardController(EmployeeRepository employeeRepository,
                               LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @GetMapping("/hr-stats")
    public ResponseEntity<HrDashboardStatsDTO> getHrDashboardStats() {
        long totalEmployees = employeeRepository.count();
        long pendingLeaves = leaveRequestRepository.countByStatusIgnoreCase("Pending");
        long onLeaveToday = leaveRequestRepository.countEmployeesOnLeaveToday(LocalDate.now());

        return ResponseEntity.ok(new HrDashboardStatsDTO(totalEmployees, pendingLeaves, onLeaveToday));
    }
}