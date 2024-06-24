package com.twat.detalks.oauth2.service;

import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.member.vo.Role;
import com.twat.detalks.member.vo.Social;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.oauth2.dto.GoogleResponse;
import com.twat.detalks.oauth2.dto.OAuth2Response;
import com.twat.detalks.oauth2.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(
        MemberRepository memberRepository
    ) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }
        // 소셜 유저 고유값
        String SocialMemberPwd = oAuth2Response.getProvider() + "_SOCIAL_" + oAuth2Response.getProviderId();
        MemberEntity existData = memberRepository.findByMemberPwd(SocialMemberPwd);

        if (existData == null) {
            // 기존 소셜 로그인 유저가 아닐경우
            // DB에 저장

            MemberEntity save = memberRepository.save(MemberEntity.builder()
                .memberEmail(oAuth2Response.getEmail())
                .memberPwd(SocialMemberPwd)
                .memberName(oAuth2Response.getName())
                .memberImg("default" + (int) (Math.random() * 5 + 1) + ".png")
                .memberSocial(Social.GOOGLE)
                .build());

            UserDTO userDTO = new UserDTO();
            userDTO.setIdx(String.valueOf(save.getMemberIdx()));
            userDTO.setUsername(SocialMemberPwd);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(Role.USER.getKey());
            return new CustomUserDetail(userDTO);
        } else {
            // 기존 유저일 경우 업데이트

            memberRepository.save(existData.toBuilder()
                .memberEmail(oAuth2Response.getEmail())
                .memberName(oAuth2Response.getName())
                .memberRole(existData.getMemberRole())
                .build());

            UserDTO userDTO = new UserDTO();
            userDTO.setIdx(String.valueOf(existData.getMemberIdx()));
            userDTO.setUsername(existData.getMemberPwd());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getMemberRole().getKey());
            return new CustomUserDetail(userDTO);
        }
    }
}
