package com.javarush.reviewplatform.config;

import com.javarush.reviewplatform.auth.JwtAuthenticationFilter;
import com.javarush.reviewplatform.auth.JwtCookieAuthenticationHandler;
import com.javarush.reviewplatform.auth.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/login", "/auth/login", "/register", "/logout", "/register").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(
                        fl -> fl
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .successHandler(jwtAuthenticationHandler())
                                .failureUrl("/login?error")
                                .permitAll()
                )
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("token")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtCookieAuthenticationHandler jwtAuthenticationHandler() {
        return new JwtCookieAuthenticationHandler(jwtProvider());
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {return new JwtAuthenticationFilter();}
}