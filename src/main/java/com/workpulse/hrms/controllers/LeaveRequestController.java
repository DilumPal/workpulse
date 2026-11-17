package com.workpulse.hrms.controllers;

import com.workpulse.hrms.dto.*;
import com.workpulse.hrms.services.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    // Employees can create a leave request for themselves
    @PostMapping
    public ResponseEntity<LeaveRequestResponseDTO> createRequest(@Valid @RequestBody LeaveRequestCreateDTO dto, Principal principal) {
        LeaveRequestResponseDTO response = leaveRequestService.createLeaveRequest(principal.getName(), dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Employees can view their own leave history
    @GetMapping("/me")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getMyRequests(Principal principal) {
        return ResponseEntity.ok(leaveRequestService.getMyLeaveRequests(principal.getName()));
    }

    // HR/Admins can view ALL leave requests across the company
    @GetMapping
    public ResponseEntity<List<LeaveRequestResponseDTO>> getAllRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    // HR/Admins can approve or reject a request
    @PutMapping("/{id}/status")
    public ResponseEntity<LeaveRequestResponseDTO> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody LeaveRequestStatusUpdateDTO dto, 
            Principal principal) {
        
        LeaveRequestResponseDTO response = leaveRequestService.updateLeaveStatus(id, principal.getName(), dto);
        return ResponseEntity.ok(response);
    }
}