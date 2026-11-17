package com.workpulse.hrms.controllers;

import com.workpulse.hrms.dto.AttendanceResponseDTO;
import com.workpulse.hrms.services.AttendanceLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/attendance-logs")
public class AttendanceLogController {

    private final AttendanceLogService attendanceService;

    public AttendanceLogController(AttendanceLogService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Employee punches in for the day
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponseDTO> checkIn(Principal principal) {
        AttendanceResponseDTO response = attendanceService.checkIn(principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Employee punches out for the day
    @PutMapping("/check-out")
    public ResponseEntity<AttendanceResponseDTO> checkOut(Principal principal) {
        AttendanceResponseDTO response = attendanceService.checkOut(principal.getName());
        return ResponseEntity.ok(response);
    }

    // Employee views their own punch history
    @GetMapping("/me")
    public ResponseEntity<List<AttendanceResponseDTO>> getMyAttendance(Principal principal) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(principal.getName()));
    }
}