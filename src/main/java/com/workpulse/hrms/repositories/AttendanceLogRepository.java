package com.workpulse.hrms.repositories;

import com.workpulse.hrms.entities.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Integer> {
}