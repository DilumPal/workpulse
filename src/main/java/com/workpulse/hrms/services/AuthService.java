package com.workpulse.hrms.services;

import com.workpulse.hrms.dto.AuthResponse;
import com.workpulse.hrms.dto.LoginRequest;
import com.workpulse.hrms.entities.User;
import com.workpulse.hrms.repositories.UserRepository;
import com.workpulse.hrms.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(User requestUser) {
        if (userRepository.findByEmail(requestUser.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Hash the plaintext password before saving to the database
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        
        // If not provided in the payload, default to a standard employee role
        if (requestUser.getRole() == null) {
            requestUser.setRole("ROLE_EMPLOYEE"); 
        }
        
        User savedUser = userRepository.save(requestUser);
        String token = jwtUtil.generateToken(savedUser.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());

        return response;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify the raw Postman password against the hashed database password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}