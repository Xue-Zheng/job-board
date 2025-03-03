package com.etu.jobboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .authorizeHttpRequests(auth -> auth
                        // Public pages
                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/css/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/js/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/images/**")).permitAll()
                        // H2 console - Using AntPathRequestMatcher
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        // Job-related page permissions
                        .requestMatchers(mvcMatcherBuilder.pattern("/jobs/create")).hasAnyRole("EMPLOYER", "ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/jobs/*/edit")).hasAnyRole("EMPLOYER", "ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/jobs/*/delete")).hasAnyRole("EMPLOYER", "ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/jobs/*/apply")).hasRole("JOB_SEEKER")
                        .requestMatchers(mvcMatcherBuilder.pattern("/applications/manage")).hasAnyRole("EMPLOYER", "ADMIN")
                        // All other pages require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // Enable H2 console access
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()
                        )
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
