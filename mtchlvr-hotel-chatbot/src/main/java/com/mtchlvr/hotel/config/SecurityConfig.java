package com.mtchlvr.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class SecurityConfig {

@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(a -> a
                .requestMatchers("/", "/rooms", "/register", "/verify", "/login",
                        "/css/**", "/js/**", "/error").permitAll()
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/customer/**", "/api/chat/**").hasRole("CUSTOMER")
                .anyRequest().authenticated())
            .formLogin(f -> f
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    boolean manager = authentication.getAuthorities().stream()
                            .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

                    if (manager) {
                        response.sendRedirect(request.getContextPath() + "/manager");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/customer/reservations");
                    }
                })
                .permitAll())
            .logout(l -> l.logoutSuccessUrl("/").permitAll())
            .csrf(c -> c.ignoringRequestMatchers("/api/chat"));

        return http.build();
    }
}