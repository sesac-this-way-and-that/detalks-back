package com.twat.detalks.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // [session 인증 방식]
            // 1. http session 에 존재하는 userId 값을 확인해서
            // 2.userId 가 존재하면, SecurityContextHolder 에 UserNamePasswordAuthToken 만들어서 저장

            // [token 인증 방식]
            // 1. request header 에 담겨온 token 을 가져와서 유효한지 확인
            String token = parseBearerToken(request);
            log.warn("filter token check {}", token);

            if(token != null  && !token.equalsIgnoreCase("null")) {
                // 대소문자 무시하고, 값이 문자열 "null"인 경우 예외처리
                String userId = tokenProvider.validateAndGetUserId(token);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    AuthorityUtils.NO_AUTHORITIES);
                // 2. SecurityContextHolder 에 UserNamePasswordAuthToken 만들어서 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // 그렇지 않을 경우 -> 예외 발생
            log.error("no Authentication {}",e.getMessage());
        }
        filterChain.doFilter(request,response);
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
}
