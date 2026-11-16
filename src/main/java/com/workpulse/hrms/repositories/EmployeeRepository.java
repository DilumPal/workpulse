package com.workpulse.hrms.repositories;

import com.workpulse.hrms.entities.Employee;
import com.workpulse.hrms.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID; 

@Repository 
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByUser(User user);
}