package com.twat.detalks.config;

import com.twat.detalks.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 메소드에 다는 어노테이션. 스프링 컨테이너가 관리할 수 있도록 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(sessionManagement->sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // IF_REQUIRED 가 디폴트
            // 토큰 인증 인가 방식에서는 session 을 사용하지 않기 때문에 설정
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.PATCH,"/api/member").authenticated()
                .requestMatchers(HttpMethod.DELETE,"/api/member").authenticated()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 원본에서의 요청을 허용하는 설정
        config.setAllowedMethods(Arrays.asList("HEAD", "POST", "PATCH", "DELETE", "PUT", "GET"));
        config.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더의 요청을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}