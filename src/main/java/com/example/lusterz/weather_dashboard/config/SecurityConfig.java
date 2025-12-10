package com.example.lusterz.weather_dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            //disable cors to allow access from frontend
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                //allow acces to login and signup pages
                .requestMatchers("/login", "/signup","/login.html", "/signup.html").permitAll()
                //allow access to adding new user endpoint
                .requestMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
                //allow access to authentication endpoint
                .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                //allow access to get all  users endpoint only to the admin
                .requestMatchers(HttpMethod.GET, "/api/v1/user").hasAuthority("ROLE_ADMIN")
                //authentication required for all other requests
                .anyRequest().authenticated()
            ).sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );
        return http.build();
    }

}
