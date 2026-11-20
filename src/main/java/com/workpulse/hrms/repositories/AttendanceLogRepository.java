package com.workpulse.hrms.repositories;

import com.workpulse.hrms.entities.AttendanceLog;
import com.workpulse.hrms.entities.Employee;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Integer> {
    // Finds a specific log for today to verify if the employee has already checked in
    Optional<AttendanceLog> findByEmployeeAndWorkDate(Employee employee, LocalDate workDate);
    
    // Retrieves the employee's entire punch history, newest first
    List<AttendanceLog> findByEmployeeOrderByWorkDateDesc(Employee employee);
}