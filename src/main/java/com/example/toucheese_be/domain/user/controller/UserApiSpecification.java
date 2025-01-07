package com.example.toucheese_be.domain.user.controller;

import com.example.toucheese_be.domain.user.dto.request.OAuthSignInDto;
import com.example.toucheese_be.domain.user.dto.response.SocialLoginDto;
import com.example.toucheese_be.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

public interface UserApiSpecification {
    @Tag(name = "회원가입", description = "회원가입 API")
    @Operation(
            summary = "소셜 로그인",
            description = "<p>소셜 Provider (구글, 카카오, 애플) 리소스 서버로 부터 받아온 사용자 정보를 가지고 요청</p>"
                    + "<p>로그인 성공 시 JWT(accessToken, refreshToken)를 발급됩니다. </p> "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공 응답",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "201", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/sign-in/oauth")
    CommonResponse<SocialLoginDto> oAuthSignIn(OAuthSignInDto dto);
}
