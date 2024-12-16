package com.example.toucheese_be.domain.auth.user.service;

import com.example.toucheese_be.domain.auth.user.constant.Role;
import com.example.toucheese_be.domain.auth.user.constant.SocialType;
import com.example.toucheese_be.domain.auth.user.dto.oauth2.GoogleResponse;
import com.example.toucheese_be.domain.auth.user.dto.oauth2.NaverResponse;
import com.example.toucheese_be.domain.auth.user.dto.oauth2.OAuth2Response;
import com.example.toucheese_be.domain.auth.user.entity.PrincipalDetails;
import com.example.toucheese_be.domain.auth.user.entity.User;
import com.example.toucheese_be.domain.auth.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthorizationException {
        // userRequest : 리소스 서버에서 받은 유저 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // registrationId 가져오기 (naver, google 중 어디에서 온 요청인지)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null; // attributes
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        // 로그인
        Optional<User> targetUser = userRepository.findByEmail(oAuth2Response.getEmail());
        // 로그인을 한적이 있는 경우
        if (targetUser.isPresent()) {
            log.info("{} 로그인을 한 적이 있습니다.", oAuth2Response.getProvider());
            User existUser = targetUser.get();
            existUser.setUsername(oAuth2Response.getName());
            existUser.setProfileImg(oAuth2Response.getProfileUrl());
            userRepository.save(existUser);

            return PrincipalDetails.builder()
                    .userId(existUser.getId())
                    .username(existUser.getUsername())
                    .email(existUser.getEmail())
                    .authorities(oAuth2Response.getProvider())
                    .build();
        }
        // 최초 로그인
        else {
            log.info("{} 로그인이 최초 입니다.", oAuth2Response.getProvider());
            User newUser = userRepository.save(User.builder()
                    .email(oAuth2Response.getEmail())
                    .username(oAuth2Response.getName())
                    .socialType(SocialType.valueOf(registrationId))
                    .profileImg(oAuth2Response.getProfileUrl())
                    .role(Role.MEMBER)
                    .build());

            return PrincipalDetails.builder()
                    .userId(newUser.getId())
                    .username(newUser.getUsername())
                    .email(newUser.getEmail())
                    .authorities(oAuth2Response.getProviderId())
                    .build();
        }
    }
}