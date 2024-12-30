package com.example.toucheese_be.domain.user.service;

import com.example.toucheese_be.domain.user.constant.Role;
import com.example.toucheese_be.domain.user.constant.SocialProvider;
import com.example.toucheese_be.domain.user.dto.request.NicknameCheck;
import com.example.toucheese_be.domain.user.dto.request.OAuthSignInDto;
import com.example.toucheese_be.domain.user.dto.request.UpdateUserDto;
import com.example.toucheese_be.domain.user.dto.response.SocialLoginDto;
import com.example.toucheese_be.domain.user.entity.PrincipalDetails;
import com.example.toucheese_be.domain.user.jwt.JwtTokenUtils;
import com.example.toucheese_be.domain.user.dto.request.CreateUserDto;
import com.example.toucheese_be.domain.user.dto.request.SignInDto;
import com.example.toucheese_be.domain.user.dto.response.UserDto;
import com.example.toucheese_be.domain.user.entity.User;
import com.example.toucheese_be.domain.user.jwt.TokenRequestDto;
import com.example.toucheese_be.domain.user.jwt.TokenResponseDto;
import com.example.toucheese_be.domain.user.repository.UserRepository;
import com.example.toucheese_be.global.common.AuthenticationFacade;
import com.example.toucheese_be.global.common.CommonResponse;
import com.example.toucheese_be.global.error.ErrorCode;
import com.example.toucheese_be.global.error.GlobalCustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationFacade authFacade;

    /**
     * 일반 회원가입
     *
     * @param dto
     * @return
     */
    public UserDto signUp(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GlobalCustomException(ErrorCode.SIGN_UP_DUPLICATED_EMAIL);
        }
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new GlobalCustomException(ErrorCode.SIGN_UP_PASSWORD_CHECK_NOT_MATCH);
        }

        // 성명, 프로필 이미지, 휴대폰 번호는 null 인채로 생성
        User savedUser = userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .role(Role.MEMBER)
                .build());

        return UserDto.fromEntity(savedUser);
    }

    /**
     * 일반 로그인
     *
     * @param dto
     * @return
     */
    public String signIn(SignInDto dto) {
        // 로그인 시만 DB 접근 (이외의 요청은 DB 접근 X)
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.SIGN_IN_EMAIL_NOT_FOUND));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new GlobalCustomException(ErrorCode.SIGN_IN_PASSWORD_NOT_MATCH);
        }

        // JWT 발급
        // 아래 코드가 유효하려면 회원가입 시점에 PrincipalDetails 가 만들어지는 코드를 추가해야함
        PrincipalDetails principalDetails = loadUserByUsername(user.getEmail());
        return jwtTokenUtils.generateAccessToken(principalDetails);
    }

    /**
     * 소셜 로그인
     *
     * @param dto
     * @return
     */
    public CommonResponse<SocialLoginDto> oAuthSignIn(OAuthSignInDto dto) {
        // socialId 존재 유무 판단
        Optional<User> optionalUser = userRepository.findBySocialId(dto.getSocialId());
        PrincipalDetails principalDetails;
        boolean isNewUser;
        User user;

        // 로그인을 한적이 있는 경우
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            log.info("이전에 로그인을 한 적이 있습니다. (userId : {})", user.getId());

            if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
                user.setEmail(dto.getEmail());
            }
            if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
                user.setUsername(dto.getUsername());
            }
            userRepository.save(user);
            principalDetails = new PrincipalDetails(user);
            isNewUser = false;
        }
        // 최초 로그인
        else {
            String userEmail = "none";
            if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
                userEmail = dto.getEmail();
            } else {
                userEmail = dto.getSocialId() + "@private.com";
            }

            user = userRepository.save(User.builder()
                    .socialId(dto.getSocialId())
                    .email(userEmail)
                    .username(dto.getUsername())
                    .socialProvider(SocialProvider.valueOf(dto.getSocialProvider()))
                    .role(Role.MEMBER)
                    .build());
            log.info("최초 로그인 입니다. (userId : {})", user.getId());
            principalDetails = new PrincipalDetails(user);
            isNewUser = true;
        }

        String accessToken = jwtTokenUtils.generateAccessToken(principalDetails);
        String refreshToken = jwtTokenUtils.generateRefreshToken();
        refreshTokenService.saveRefreshToken(principalDetails.getEmail(), refreshToken);

        TokenResponseDto tokens = TokenResponseDto.builder()
                .accessToken(accessToken)
                .issuedAt(jwtTokenUtils.getClaims(accessToken).getIssuedAt())
                .expiration(jwtTokenUtils.getClaims(accessToken).getExpiration())
                .refreshToken(refreshToken)
                .build();

        SocialLoginDto socialLoginDto = SocialLoginDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getPhone())
                .phone(user.getPhone())
                .profileImg(user.getProfileImg())
                .role(user.getRole())
                .socialProvider(user.getSocialProvider())
                .tokens(tokens)
                .build();


        if (isNewUser) {
            return CommonResponse.created(socialLoginDto);
        } else {
            return CommonResponse.ok(socialLoginDto);
        }
    }

    /**
     * accessToken, refreshToken 재발급
     *
     * @param dto
     * @return
     */
    public SocialLoginDto refreshAccessToken(TokenRequestDto dto) {
        String accessToken = dto.getAccessToken();
        String refreshToken = dto.getRefreshToken();
        String email = jwtTokenUtils.extractEmailFromToken(accessToken);

        String storedRefreshToken = refreshTokenService.getRefreshToken(email);
        // refresh token 불일치
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new GlobalCustomException(ErrorCode.REFRESH_TOKEN_NOT_EQAUL);
        }
        // refresh token 일치
        else {
            // 새로운 ATK, RTK 생성
            PrincipalDetails principalDetails = loadUserByUsername(email);
            String newAccessToken = jwtTokenUtils.generateAccessToken(principalDetails);
            String newRefreshToken = jwtTokenUtils.generateRefreshToken();

            refreshTokenService.deleteRefreshToken(email);
            refreshTokenService.saveRefreshToken(email, newRefreshToken);

            log.info("새로운 Access Token 및 Refresh Token 발급 성공. 사용지: {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new GlobalCustomException(ErrorCode.USER_NOT_FOUND));

            TokenResponseDto tokens = TokenResponseDto.builder()
                    .accessToken(newAccessToken)
                    .issuedAt(jwtTokenUtils.getClaims(newAccessToken).getIssuedAt())
                    .expiration(jwtTokenUtils.getClaims(newAccessToken).getExpiration())
                    .refreshToken(newRefreshToken)
                    .build();

            // 응답 DTO
            return SocialLoginDto.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .nickname(user.getPhone())
                    .phone(user.getPhone())
                    .role(user.getRole())
                    .socialProvider(user.getSocialProvider())
                    .tokens(tokens)
                    .build();

        }
    }

    /**
     * profile update
     *
     * @return
     */
    public Boolean profileUpdate(UpdateUserDto dto) {
        // 현재 인증된 사용자 정보
        PrincipalDetails principalDetails = authFacade.getAuth();

        User user = userRepository.findById(principalDetails.getUserId())
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.USER_NOT_FOUND));

        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            user.setNickname(dto.getNickname());
        }
        userRepository.save(user);
        return true;
    }

    /**
     * 닉네임 중복 체크
     * @param dto
     * @return
     */
    public Boolean checkNickname(NicknameCheck dto) {
        return userRepository.findByNickname(dto.getNickname()).isPresent();
    }


    /**
     * get user details
     *
     * @return
     */
    public CommonResponse<UserDto> getUserDetails() {
        PrincipalDetails principalDetails = authFacade.getAuth();
        User user = userRepository.findById(principalDetails.getUserId())
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.USER_NOT_FOUND));
        return CommonResponse.ok(UserDto.fromEntity(user));
    }

    @Override
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.USER_NOT_FOUND));
        return PrincipalDetails.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user.getRole().getRoles())
                .build();
    }


}
