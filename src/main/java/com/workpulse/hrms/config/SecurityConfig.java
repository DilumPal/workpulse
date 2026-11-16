package com.workpulse.hrms.config;

import com.workpulse.hrms.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        
                        // Department Endpoints (Admin only)
                        .requestMatchers("/api/departments/**").hasAuthority("ROLE_ADMIN")

                        // Employee Self-Service 
                        .requestMatchers(HttpMethod.PUT, "/api/employees/me").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")

                        // General Employee Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/employees/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER")

                        // Leave Request Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/leave-requests/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/leave-requests/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")
                        // Only Admins and HR Managers can approve/reject/update a leave request
                        .requestMatchers(HttpMethod.PUT, "/api/leave-requests/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER")

                        // Attendance Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/attendance-logs/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/attendance-logs/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")

                        .anyRequest().authenticated())
                
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}