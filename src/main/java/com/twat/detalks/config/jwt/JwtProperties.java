package com.twat.detalks.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
// 자바 클래스 필드와 프로퍼티 값(application.properties) 파일을 매핑할때 사용하는 어노테이션
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
