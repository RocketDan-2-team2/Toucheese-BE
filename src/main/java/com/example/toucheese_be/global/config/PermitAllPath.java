package com.example.toucheese_be.global.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PermitAllPath {
    String[] paths = {
            "/api/v1/studio/**",
            "/api/v1/order/**",
            "/api/v1/auth/**",
            "/api/v1/admin/**"
    };
}
