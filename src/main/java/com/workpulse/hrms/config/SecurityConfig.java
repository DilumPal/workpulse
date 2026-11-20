package com.workpulse.hrms.config;

import com.workpulse.hrms.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        
                        // Department Endpoints (Read for all roles; Create/Update/Delete for Admin only)
                        .requestMatchers(HttpMethod.GET, "/api/departments", "/api/departments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")
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
                        .requestMatchers(HttpMethod.PUT, "/api/attendance-logs/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_EMPLOYEE")

                        .anyRequest().authenticated())
                
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Whitelist the local frontend URLs
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        
        // Allow the standard HTTP methods, plus OPTIONS (which browsers use for pre-flight checks)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Allow the frontend to send JSON and the JWT Authorization header
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        
        // Allow credentials 
        configuration.setAllowCredentials(true);

        // Apply these rules to every endpoint in the API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}