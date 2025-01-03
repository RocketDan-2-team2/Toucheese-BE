package com.example.toucheese_be.global.config;

import com.example.toucheese_be.domain.user.jwt.JwtTokenFilter;
import com.example.toucheese_be.domain.user.jwt.JwtTokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api/v1/studio/**",
                                "/api/v1/auth/sign-up/**",
                                "/api/v1/auth/sign-in/**",
                                "/api/v1/admin/**",
                                "/api/v1/auth/details",
                                "/api/v1/auth/nickname/check"
                        )
                        .permitAll()
                        .requestMatchers(
                                "/api/v1/toss/**",
                                "/api/v1/auth/profile/update",
                                "/api/v1/auth/reissuance",
                                "/api/v1/order/**",
                                "/api/v1/auth/profile/update"
                        )
                        .authenticated()
                        .requestMatchers(
                                "/api/v1/order/**",
                                "/api/v1/auth/profile/update",
                                "/api/v1/auth/reissuance"
                        )
                        .hasAuthority("ROLE_MEMBER")
//                        .requestMatchers(
//                                "/api/v1/admin/**"
//                        ).hasAuthority("ROLE_ADMIN")
                )
                // JWT를 사용하기 때문에 보안 관련 세션 해제
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT 필터를 권한 필터 앞에 삽입
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenUtils),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
}
