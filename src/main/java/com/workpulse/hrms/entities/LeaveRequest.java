package com.workpulse.hrms.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "leave_requests")
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Integer leaveId;

    @NotNull(message = "Employee assignment is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @NotBlank(message = "Leave type is required")
    @Column(name = "leave_type", columnDefinition = "leave_category")
    @ColumnTransformer(write = "?::leave_category")
    private String leaveType;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Requested duration is required")
    @Positive(message = "Duration must be greater than zero")
    @Column(name = "requested_duration", precision = 4, scale = 1)
    private BigDecimal requestedDuration;

    @NotBlank(message = "Reason is required")
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @NotBlank(message = "Status is required")
    @Column(name = "status", columnDefinition = "leave_status")
    @ColumnTransformer(write = "?::leave_status")
    private String status;

    @Column(name = "reviewed_by")
    private UUID reviewedBy; 

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}