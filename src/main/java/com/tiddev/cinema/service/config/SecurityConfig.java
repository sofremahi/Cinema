package com.tiddev.cinema.service.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/homepage.html", "/login.html","/signin.html", "/logout.html", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/rest/cinema/salon0/add/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rest/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.PUT, "/rest/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, "/rest/**").hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/login.html")
                        .defaultSuccessUrl("/user.html", true)
                        .loginProcessingUrl("/login")
                        .failureHandler(customAuthenticationFailureHandler()) // Use a custom failure handler
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout.html?success")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                String errorMessage;
                if (exception.getMessage().equalsIgnoreCase("User not found")) {
                    errorMessage = "username";
                } else if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
                    errorMessage = "password";
                } else {
                    errorMessage = "unknown";
                }
                response.sendRedirect("/login.html?error=true&errorType=" + errorMessage);
            }
        };
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
