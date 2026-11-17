package com.workpulse.hrms.repositories;

import com.workpulse.hrms.entities.Employee;
import com.workpulse.hrms.entities.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByEmployee(Employee employee);
}