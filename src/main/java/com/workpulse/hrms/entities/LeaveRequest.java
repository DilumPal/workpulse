package com.workpulse.hrms.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @Column(name = "leave_type", columnDefinition = "leave_category")
    @ColumnTransformer(write = "?::leave_category")
    private String leaveType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "requested_duration", precision = 4, scale = 1)
    private BigDecimal requestedDuration;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "status", columnDefinition = "leave_status")
    @ColumnTransformer(write = "?::leave_status")
    private String status;

    @Column(name = "reviewed_by")
    private UUID reviewedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}