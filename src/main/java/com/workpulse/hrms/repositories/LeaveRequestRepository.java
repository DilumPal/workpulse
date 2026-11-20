package com.workpulse.hrms.repositories;

import com.workpulse.hrms.entities.Employee;
import com.workpulse.hrms.entities.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByEmployee(Employee employee);

    @Query(value = "SELECT COUNT(*) FROM leave_requests WHERE LOWER(CAST(status AS TEXT)) = LOWER(:status)", nativeQuery = true)
    long countByStatusIgnoreCase(@Param("status") String status);

    @Query(value = "SELECT COUNT(DISTINCT employee_id) FROM leave_requests " +
            "WHERE LOWER(CAST(status AS TEXT)) = 'approved' AND :today BETWEEN start_date AND end_date", nativeQuery = true)
    long countEmployeesOnLeaveToday(@Param("today") LocalDate today);
}