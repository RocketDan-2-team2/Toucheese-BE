package com.example.toucheese_be.domain.user.jwt;


import com.example.toucheese_be.global.common.CommonResponse;
import com.example.toucheese_be.global.error.ErrorCode;
import com.example.toucheese_be.global.error.GlobalCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePath = {
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui/index.html/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/swagger-ui.html",
                "/public/**",
                "/api/v1/studio/**",
                "/api/v1/auth/sign-up/**",
                "/api/v1/auth/sign-in/**",
                "/api/v1/auth/nickname/check"
        };
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            log.info("JWT 검증 시작");
            String token = resolveToken(request);
            if (token != null && jwtTokenUtils.validate(token)) {
                Authentication authentication = jwtTokenUtils.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("인증된 사용자: {}", authentication.getName());
            }

            filterChain.doFilter(request, response);
        } catch (GlobalCustomException e) {
            log.error("JWT 검증 실패: {}", e.getMessage());
            sendErrorResponse(response, e.getErrorCode());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        CommonResponse<Object> errorResponse = CommonResponse.fail(errorCode);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(errorResponse));
            writer.flush();
        }
    }
}


