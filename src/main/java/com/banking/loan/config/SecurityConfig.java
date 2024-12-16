package com.banking.loan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disable CSRF for simplicity
                .authorizeRequests()
                .anyRequest().hasRole("ADMIN") // Require ADMIN role for all endpoints
                .and()
                .httpBasic(); // Enable Basic Authentication
        return http.build();
    }
}