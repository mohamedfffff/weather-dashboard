package com.example.lusterz.weather_dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                //allow access to get all  users endpoint only to the admin
                .requestMatchers(HttpMethod.GET, "/api/v1/user").hasAuthority("ROLE_ADMIN")
                //authentication required for all other requests
                .anyRequest().authenticated()
            )
            //use simple username and password login auth
            .httpBasic(Customizer.withDefaults())
            //use custom login form
            .formLogin(form -> form
                .loginPage("/login.html")
                .defaultSuccessUrl("/home.html")
                .failureUrl("/login.html?error=true")
            );
        return http.build();
    }

}
