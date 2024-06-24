package com.twat.detalks.oauth2.jwt;

import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.oauth2.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

        try {
            String token = parseBearerToken(request);

            if (token != null && !token.equalsIgnoreCase("null")) {

                // 토큰에서 username과 role 획득
                UserDTO userDTO = new UserDTO();
                userDTO.setIdx(jwtUtil.getIdx(token));
                userDTO.setRole(jwtUtil.getRole(token));
                // // UserDetails에 회원 정보 객체 담기
                CustomUserDetail customUserDetail = new CustomUserDetail(userDTO);
                // 스프링 시큐리티 인증 토큰 생성
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetail, null, customUserDetail.getAuthorities());
                // 세션에 사용자 등록
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // 그렇지 않을 경우 -> 예외 발생
            log.warn("인증 오류 예외 발생 {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}