package com.twat.detalks.member.controller;

import com.twat.detalks.member.dto.*;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.oauth2.jwt.JWTUtil;
import com.twat.detalks.question.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "회원 API (로그인 유저)", description = "로그인 회원만 사용가능한 API")
@Slf4j
public class MemberAuthController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final BookmarkService bookmarkService;

    // GET http://localhost:8080/api/member/auth
    // 회원정보조회 (로그인 유저)
    @GetMapping("/auth")
    @Operation(summary = "회원 정보 조회 (로그인 유저)")
    public ResponseEntity<?> getMemberAuth(@AuthenticationPrincipal CustomUserDetail user) {
        String idx = user.getUserIdx();
        MemberEntity result = memberService.findByMemberId(idx);
        long questionCount = memberService.getQuestionCount(idx);
        long answerCount = memberService.getAnswerCount(idx);
        List<String> tags = memberService.getTags(idx);
        Long bookmarkCount = bookmarkService.countBookMark(result.getMemberIdx());
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
            .tags(tags)
            .created(result.getMemberCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .visited(result.getMemberVisited().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .role(result.getMemberRole().getKey())
            .bookmarkCount(bookmarkCount)
            .build();
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 조회 (로그인 유저) 성공")
                .result(true)
                .data(data)
                .build());
    }

    // PATCH http://localhost:8080/api/member/auth
    // 회원정보수정
    // 이름(필수), 한줄소개, 자기소개
    @PatchMapping("/auth")
    @Operation(summary = "회원 정보 수정")
    public ResponseEntity<?> updateMemberAuth(
        @AuthenticationPrincipal CustomUserDetail user,
        @RequestBody @Valid MemberUpdateDto memberUpdateDto) {
        memberService.updateMember(user.getUserIdx(), memberUpdateDto);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 수정 성공")
                .result(true)
                .status("200")
                .build());
    }

    // PATCH http://localhost:8080/api/member/auth/profile
    // 회원 프로필 이미지 수정
    // 프로필 이미지 경로(필수)
    @PatchMapping(value = "/auth/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원 프로필 이미지 수정")
    public ResponseEntity<?> updateImgAuth(
        @AuthenticationPrincipal CustomUserDetail user,

        @RequestPart(required = false) MultipartFile img) {
        memberService.updateImg(user.getUserIdx(), img);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 프로필 이미지 수정 성공")
                .result(true)
                .status("200")
                .build());
    }

    // POST http://localhost:8080/api/member/auth
    // 회원탈퇴복구
    @PostMapping("/auth")
    @Operation(summary = "회원 탈퇴 복구")
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
    // 비밀번호(필수), 탈퇴 사유
    @DeleteMapping("/auth")
    @Operation(summary = "회원 탈퇴 (일반회원)")
    public ResponseEntity<?> deleteMemberAuth(@AuthenticationPrincipal CustomUserDetail user,
                                              @RequestBody @Valid MemberDeleteDto memberDeleteDto) {
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
    // 탈퇴 사유
    @DeleteMapping("/auth/social")
    @Operation(summary = "회원 탈퇴 (소셜회원)")
    public ResponseEntity<?> deleteSocialMemberAuth(@AuthenticationPrincipal CustomUserDetail user,
                                                    @Schema(description = "탈퇴사유", example = "더 이상 서비스를 이용하지 않습니다.")
                                                    @RequestBody String reason) {
        memberService.deleteSocialMember(user.getUserIdx(), reason);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 탈퇴 (소셜회원) 성공")
                .result(true)
                .status("200")
                .build());
    }


    // PATCH http://localhost:8080/api/member/auth/password
    // 비밀번호변경
    // 현재 비밀번호(필수), 바꿀 비밀번호(필수)
    // 소셜 로그인 제외
    @PatchMapping("/auth/password")
    @Operation(summary = "비밀번호 변경 (일반회원)")
    public ResponseEntity<?> changePassword(
        @AuthenticationPrincipal CustomUserDetail user,
        @RequestBody @Valid ChangePwdDto changePwdDto) {
        memberService.changePassword(user.getUserIdx(), changePwdDto.getPwd(), changePwdDto.getChangePwd());
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
    @Operation(summary = "소셜 로그인 유저 쿠키값 헤더 토큰 저장",
        description = "쿠키에 있는 토큰을 백엔드로 다시 보내서 헤더로 받아오는 API (스웨거 쿠키 설정 이슈로 미작동중)")
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
