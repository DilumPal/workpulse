package com.workpulse.hrms.repositories;

import com.workpulse.hrms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository 
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    
}
