package com.twat.detalks.member.controller;

import com.twat.detalks.member.dto.*;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.oauth2.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberAuthController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    // GET http://localhost:8080/api/member/auth
    // 회원정보조회 (로그인 유저)
    @GetMapping("/auth")
    public ResponseEntity<?> getMemberAuth(@AuthenticationPrincipal CustomUserDetail user) {
        MemberEntity result = memberService.findByMemberId(user.getUserIdx());
        long questionCount = memberService.getQuestionCount(user.getUserIdx());
        long answerCount = memberService.getAnswerCount(user.getUserIdx());
        MemberReadDto data = MemberReadDto.builder()
            .idx(result.getMemberIdx())
            .email(result.getMemberEmail())
            .name(result.getMemberName())
            .isDeleted(result.getMemberIsDeleted())
            .reason(result.getMemberReason())
            .state(result.getMemberState())
            .img(result.getMemberImg())
            .summary(result.getMemberSummary())
            .about(result.getMemberAbout())
            .rep(result.getMemberRep())
            .social(result.getMemberSocial())
            .qCount(questionCount)
            .aCount(answerCount)
            .created(result.getMemberCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .visited(result.getMemberVisited().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .role(result.getMemberRole().getKey())
            .build();
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 조회(로그인 유저) 성공")
                .result(true)
                .data(data)
                .build());
    }

    // PATCH http://localhost:8080/api/member/auth
    // 회원정보수정
    // 폼전송
    // 이름(필수), 프로필 이미지 경로(필수), 한줄소개, 자기소개
    @PatchMapping("/auth")
    public ResponseEntity<?> updateMemberAuth(
        @AuthenticationPrincipal CustomUserDetail user,
        @Valid MemberUpdateDto memberUpdateDto,
        @RequestPart(required = false) MultipartFile img) {
        memberService.updateMember(user.getUserIdx(), memberUpdateDto, img);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 수정 성공")
                .result(true)
                .status("200")
                .build());
    }

    // POST http://localhost:8080/api/member/auth
    // 회원탈퇴복구
    @PostMapping("/auth")
    public ResponseEntity<?> restoreMemberAuth(@AuthenticationPrincipal CustomUserDetail user) {
        memberService.restoreMember(user.getUserIdx());
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 복구 성공")
                .result(true)
                .status("200")
                .build());
    }

    // DELETE http://localhost:8080/api/member/auth
    // 회원탈퇴
    // 폼전송
    // 비밀번호(필수), 탈퇴 사유
    @DeleteMapping("/auth")
    public ResponseEntity<?> deleteMemberAuth(@AuthenticationPrincipal CustomUserDetail user, @Valid MemberDeleteDto memberDeleteDto) {
        memberService.deleteMember(user.getUserIdx(), memberDeleteDto);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 탈퇴 성공")
                .result(true)
                .status("200")
                .build());
    }

    // DELETE http://localhost:8080/api/member/auth/social
    // 회원탈퇴(소셜 회원)
    // 폼전송
    // 탈퇴 사유
    @DeleteMapping("/auth/social")
    public ResponseEntity<?> deleteSocialMemberAuth(@AuthenticationPrincipal CustomUserDetail user, String reason) {
        memberService.deleteSocialMember(user.getUserIdx(), reason);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 탈퇴 성공")
                .result(true)
                .status("200")
                .build());
    }


    // PATCH http://localhost:8080/api/member/auth/password
    // 비밀번호변경
    // 폼전송
    // 현재 비밀번호(필수), 바꿀 비밀번호(필수)
    // 소셜 로그인 제외
    @PatchMapping("/auth/password")
    public ResponseEntity<?> changePassword(
        @AuthenticationPrincipal CustomUserDetail user,
        @RequestParam String pwd,
        @RequestParam String changePwd) {
        memberService.changePassword(user.getUserIdx(), pwd, changePwd);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("비밀번호 변경 성공")
                .result(true)
                .status("200")
                .build());
    }


    // GET http://localhost:8080/api/member/auth/header
    // 소셜 로그인 토큰저장 쿠키 -> 헤더 변경 API
    @GetMapping("/auth/header")
    public ResponseEntity<?> changeHeader(
        HttpServletRequest request
    ) throws ServletException, IOException {

        // cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                token = cookie.getValue();
            }
        }
        // Authorization 헤더 검증
        if (token == null) {
            return ResponseEntity.badRequest().body(
                ResDto.builder()
                    .result(false)
                    .status("400")
                    .msg("쿠키값이 존재하지 않습니다.")
                    .build());
        }
        // 토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            return ResponseEntity.badRequest().body(
                ResDto.builder()
                    .result(false)
                    .status("400")
                    .msg("쿠키가 만료 되었습니다.")
                    .build());
        }
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("로그인 성공")
                .result(true)
                .status("200")
                .token(token)
                .build());
    }
}
