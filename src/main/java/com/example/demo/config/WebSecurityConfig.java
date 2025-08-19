package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import java.util.Collection;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .successHandler((request, response, authentication) -> {
                            Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
                            var savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                            String targetUrl = savedRequest.getRedirectUrl();
                            if (targetUrl != null && !targetUrl.equals("/") && !targetUrl.contains("continue")) {
                                response.sendRedirect(targetUrl);
                            } else if ((roles.stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getAuthority())))) {
                                response.sendRedirect("/admin");
                            }else if ((roles.stream().anyMatch(r -> "ROLE_USER".equals(r.getAuthority())))) {
                                response.sendRedirect("/user");
                            }
                        })
                )
                .logout(Customizer.withDefaults());

        return http.build();
    }
}