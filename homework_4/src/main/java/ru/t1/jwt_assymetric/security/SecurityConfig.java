package ru.t1.jwt_assymetric.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private static final String[] PUBLIC_URLS = {"/api/v1/auth/login", "/api/v1/auth/register",
      "/api/v1/auth/refresh", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
      "/swagger-resources/**", "/swagger-resources", "/configuration/ui", "/configuration/security",
      "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"};

  private final JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth
            -> auth.requestMatchers(PUBLIC_URLS)
                .permitAll()
                .requestMatchers("api/v1/auth/premium")
                .hasRole("ADMIN")
                //@PreAuthorize
                .requestMatchers("/api/v1/users/me/premium")
                .hasRole("PREMIUM_USER")
                .anyRequest()
                .hasAnyRole("ADMIN", "GUEST", "PREMIUM_USER"))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
