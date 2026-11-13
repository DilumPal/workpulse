package com.workpulse.hrms.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "role", columnDefinition = "access_role")
    @ColumnTransformer(write = "?::access_role")
    private String role;
}