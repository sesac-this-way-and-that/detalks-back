package com.twat.detalks.security;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.FileNotFoundException;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private TokenProvider tokenProvider;

    @Autowired
    public JwtAuthFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);
            log.warn("filter token check {}", token);

            if (token != null && !token.equalsIgnoreCase("null")) {

                String userId = tokenProvider.validateAndGetUserId(token);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    AuthorityUtils.NO_AUTHORITIES);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // 그렇지 않을 경우 -> 예외 발생
            log.warn("no Authentication {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
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
