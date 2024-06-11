package com.twat.detalks.oauth2.jwt;

import com.twat.detalks.oauth2.dto.CustomOAuth2User;
import com.twat.detalks.oauth2.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String parseBearerToken(HttpServletRequest request) {
        // request header 의 bearer Token 에서 jwt 토큰을 가져오는 작업
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // null, 공백이 아닌지
            return bearerToken.substring(7); // bearer 제거 후 반환
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        // String authorization = null;
        // Cookie[] cookies = request.getCookies();
        // for (Cookie cookie : cookies) {
        //     if (cookie.getName().equals("Authorization")) {
        //         authorization = cookie.getValue();
        //     }
        // }

        // Authorization 헤더 검증
        // if (authorization == null) {
        //     filterChain.doFilter(request, response);
        //     // 조건이 해당되면 메소드 종료 (필수)
        //     return;
        // }
        // 토큰
        // String token = authorization;
        // // 토큰 소멸 시간 검증
        // if (jwtUtil.isExpired(token)) {
        //     filterChain.doFilter(request, response);
        //     // 조건이 해당되면 메소드 종료 (필수)
        //     return;
        // }

        try {
            String token = parseBearerToken(request);

            log.warn("filter token check {}", token);

            if (token != null && !token.equalsIgnoreCase("null")) {

                // 토큰에서 username과 role 획득
                UserDTO userDTO = new UserDTO();
                userDTO.setIdx(jwtUtil.getIdx(token));
                userDTO.setRole(jwtUtil.getRole(token));
                // // UserDetails에 회원 정보 객체 담기
                CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
                // 스프링 시큐리티 인증 토큰 생성
                Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
                // 세션에 사용자 등록
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // 그렇지 않을 경우 -> 예외 발생
            log.warn("no Authentication {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}