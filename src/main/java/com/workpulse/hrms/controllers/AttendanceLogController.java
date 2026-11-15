package com.workpulse.hrms.controllers;

import com.workpulse.hrms.entities.AttendanceLog;
import com.workpulse.hrms.services.AttendanceLogService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-logs")
@CrossOrigin(origins = "http://localhost:5173")
public class AttendanceLogController {

    private final AttendanceLogService attendanceLogService;

    public AttendanceLogController(AttendanceLogService attendanceLogService) {
        this.attendanceLogService = attendanceLogService;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceLog>> getAllAttendanceLogs() {
        List<AttendanceLog> logs = attendanceLogService.getAllAttendanceLogs();
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AttendanceLog> createAttendanceLog(@Valid @RequestBody AttendanceLog attendanceLog) {
        AttendanceLog createdLog = attendanceLogService.createAttendanceLog(attendanceLog);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }
}