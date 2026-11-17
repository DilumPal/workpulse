package com.workpulse.hrms.services;

import com.workpulse.hrms.dto.AttendanceResponseDTO;
import com.workpulse.hrms.entities.AttendanceLog;
import com.workpulse.hrms.entities.Employee;
import com.workpulse.hrms.entities.User;
import com.workpulse.hrms.repositories.AttendanceLogRepository;
import com.workpulse.hrms.repositories.EmployeeRepository;
import com.workpulse.hrms.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceLogService {

    private final AttendanceLogRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public AttendanceLogService(AttendanceLogRepository attendanceRepository, 
                                EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AttendanceResponseDTO checkIn(String email) {
        Employee employee = getEmployeeByEmail(email);
        LocalDate today = LocalDate.now();

        // Prevent double check-ins on the same day
        Optional<AttendanceLog> existingLog = attendanceRepository.findByEmployeeAndWorkDate(employee, today);
        if (existingLog.isPresent()) {
            throw new IllegalStateException("You have already checked in today.");
        }

        AttendanceLog log = new AttendanceLog();
        log.setEmployee(employee);
        log.setWorkDate(today);
        log.setCheckInTime(LocalDateTime.now()); // Uses LocalDateTime
        
        log = attendanceRepository.save(log);
        return convertToDTO(log);
    }

    @Transactional
    public AttendanceResponseDTO checkOut(String email) {
        Employee employee = getEmployeeByEmail(email);
        LocalDate today = LocalDate.now();

        // Ensure the employee actually checked in today before trying to check out
        AttendanceLog log = attendanceRepository.findByEmployeeAndWorkDate(employee, today)
                .orElseThrow(() -> new IllegalStateException("No active check-in found for today."));

        if (log.getCheckOutTime() != null) {
            throw new IllegalStateException("You have already checked out today.");
        }

        log.setCheckOutTime(LocalDateTime.now()); // Uses LocalDateTime
        log = attendanceRepository.save(log);
        
        return convertToDTO(log);
    }

    public List<AttendanceResponseDTO> getMyAttendance(String email) {
        Employee employee = getEmployeeByEmail(email);
        return attendanceRepository.findByEmployeeOrderByWorkDateDesc(employee).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper: Find employee by JWT email
    private Employee getEmployeeByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return employeeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found"));
    }

    // Helper: Convert to safe frontend DTO
    private AttendanceResponseDTO convertToDTO(AttendanceLog log) {
        AttendanceResponseDTO dto = new AttendanceResponseDTO();
        
        // Matches the logId from your entity
        dto.setLogId(log.getLogId());
        
        dto.setWorkDate(log.getWorkDate());
        dto.setCheckInTime(log.getCheckInTime());
        dto.setCheckOutTime(log.getCheckOutTime());

        if (log.getEmployee() != null) {
            dto.setEmployeeName(log.getEmployee().getFirstName() + " " + log.getEmployee().getLastName());
        }

        if (log.getCheckOutTime() != null) {
            dto.setStatus("Checked Out");
        } else {
            dto.setStatus("Present");
        }

        return dto;
    }
}