package com.example.toucheese_be.global.config;

import com.example.toucheese_be.domain.auth.user.jwt.JwtTokenFilter;
import com.example.toucheese_be.domain.auth.user.jwt.JwtTokenUtils;
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
    private final PermitAllPath permitAllPath;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/oauth2/authorization/google") // OAuth2 로그인 페이지 설정
//                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllPath.getPaths())
                        .permitAll()
                        //.requestMatchers().hasRole("GUEST")
                        //.requestMatchers("/api/v1/studio/**").hasRole("MEMBER")
                        //.requestMatchers().hasRole("ADMIN")
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
