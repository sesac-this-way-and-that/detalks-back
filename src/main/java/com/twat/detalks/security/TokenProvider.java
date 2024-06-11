// package com.twat.detalks.security;
//
// import com.twat.detalks.config.jwt.JwtProperties;
// import com.twat.detalks.member.entity.MemberEntity;
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
//
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.Date;
//
// @Component
// public class TokenProvider {
//
//     private final JwtProperties jwtProperties;
//
//     @Autowired
//     public TokenProvider(JwtProperties jwtProperties) {
//         this.jwtProperties = jwtProperties;
//     }
//
//     public String create(MemberEntity memberEntity) {
//         Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
//         return Jwts.builder().signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
//             .subject(String.valueOf(memberEntity.getMemberIdx()))
//             .claim("memberName",memberEntity.getMemberName())
//             .issuer(jwtProperties.getSecretKey())
//             .expiration(expiredDate)
//             .issuedAt(new Date())
//             .compact();
//     }
//
//     // 입력된 token 에서 payload 에 있는 memberIdx 가져오기
//     public String validateAndGetUserId(String token) {
//         Claims claims = Jwts
//             .verifyWith(jwtProperties.getSecretKey())
//             .parseSinged(token) // 토큰이 위조되지 않았다면 payload 를 return
//             .getBody();
//         return claims.getSubject();
//     }
// }
