package com.workpulse.hrms.services;

import com.workpulse.hrms.entities.AttendanceLog;
import com.workpulse.hrms.repositories.AttendanceLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceLogService {

    private final AttendanceLogRepository attendanceLogRepository;

    public AttendanceLogService(AttendanceLogRepository attendanceLogRepository) {
        this.attendanceLogRepository = attendanceLogRepository;
    }

    public List<AttendanceLog> getAllAttendanceLogs() {
        return attendanceLogRepository.findAll();
    }

    public AttendanceLog createAttendanceLog(AttendanceLog attendanceLog) {
        return attendanceLogRepository.save(attendanceLog);
    }
}