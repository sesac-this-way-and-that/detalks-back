package com.twat.detalks.oauth2.service;

import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.member.vo.Role;
import com.twat.detalks.member.vo.Social;
import com.twat.detalks.oauth2.dto.CustomOAuth2User;
import com.twat.detalks.oauth2.dto.GoogleResponse;
import com.twat.detalks.oauth2.dto.OAuth2Response;
import com.twat.detalks.oauth2.dto.UserDTO;
import com.twat.detalks.oauth2.entity.UserEntity;
import com.twat.detalks.oauth2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(
        UserRepository userRepository,
        MemberRepository memberRepository
    ) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.warn("oAuth2User :: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }
        // String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        // log.warn("username :: {}", username);
        // UserEntity existData = userRepository.findByUsername(username);

        // 소셜 유저 고유값
        String SocialMemberPwd = oAuth2Response.getProvider() + "_SOCIAL_" + oAuth2Response.getProviderId();
        MemberEntity existData = memberRepository.findByMemberPwd(SocialMemberPwd);

        // 이메일 중복 여부 체크
        // boolean result = memberRepository.existsByMemberEmail(oAuth2Response.getEmail());
        // if (result) {
        //     throw new IllegalArgumentException("사용 불가능한 이메일 입니다.");
        // }

        if (existData == null) {
            // 기존 소셜 로그인 유저가 아닐경우
            // DB에 저장

/*            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");
            userRepository.save(userEntity);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");
            return new CustomOAuth2User(userDTO);*/

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
            return new CustomOAuth2User(userDTO);
        } else {
            // 기존 소셜 로그인 유저일 경우
            // DB에 기존 내용 업데이트
            // 이메일이나 이름 같은게 변경되었을 경우 업데이트

/*            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());
            userRepository.save(existData);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());
            return new CustomOAuth2User(userDTO);*/

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
            return new CustomOAuth2User(userDTO);
        }
    }
}
