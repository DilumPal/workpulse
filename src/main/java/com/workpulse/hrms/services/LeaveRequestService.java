package com.workpulse.hrms.services;

import com.workpulse.hrms.dto.*;
import com.workpulse.hrms.entities.*;
import com.workpulse.hrms.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, 
                               EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LeaveRequestResponseDTO createLeaveRequest(String loggedInEmail, LeaveRequestCreateDTO dto) {
        Employee employee = getEmployeeByEmail(loggedInEmail);

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(dto.getLeaveType());
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setRequestedDuration(dto.getRequestedDuration());
        request.setReason(dto.getReason());
        request.setStatus("Pending"); // Default status

        request = leaveRequestRepository.save(request);
        return convertToDTO(request);
    }

    @Transactional
    public LeaveRequestResponseDTO updateLeaveStatus(Integer leaveId, String reviewerEmail, LeaveRequestStatusUpdateDTO dto) {
        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        
        Employee reviewer = getEmployeeByEmail(reviewerEmail);

        request.setStatus(dto.getStatus());
        request.setReviewedBy(reviewer);

        request = leaveRequestRepository.save(request);
        return convertToDTO(request);
    }

    public List<LeaveRequestResponseDTO> getMyLeaveRequests(String loggedInEmail) {
        Employee employee = getEmployeeByEmail(loggedInEmail);
        return leaveRequestRepository.findByEmployee(employee).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestResponseDTO> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper: Gets the Employee entity from a JWT email
    private Employee getEmployeeByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return employeeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Employee profile not found"));
    }

    // Helper: Flattens the entity for the frontend
    private LeaveRequestResponseDTO convertToDTO(LeaveRequest request) {
        LeaveRequestResponseDTO dto = new LeaveRequestResponseDTO();
        dto.setLeaveId(request.getLeaveId());
        dto.setLeaveType(request.getLeaveType());
        dto.setStartDate(request.getStartDate());
        dto.setEndDate(request.getEndDate());
        dto.setRequestedDuration(request.getRequestedDuration());
        dto.setReason(request.getReason());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());

        if (request.getEmployee() != null) {
            dto.setEmployeeId(request.getEmployee().getEmployeeId());
            dto.setEmployeeName(request.getEmployee().getFirstName() + " " + request.getEmployee().getLastName());
        }

        if (request.getReviewedBy() != null) {
            dto.setReviewedBy(request.getReviewedBy().getFirstName() + " " + request.getReviewedBy().getLastName());
        }

        return dto;
    }
}