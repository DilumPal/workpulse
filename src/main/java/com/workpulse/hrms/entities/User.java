package com.workpulse.hrms.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password_hash", nullable = false)
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(Admin|HR Manager|Employee)$", message = "Must be 'Admin', 'HR Manager', or 'Employee'")
    @Column(name = "role", columnDefinition = "access_role")
    @ColumnTransformer(write = "?::access_role")
    private String role;
}