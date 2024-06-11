package com.twat.detalks.member.controller;

import com.twat.detalks.member.dto.MemberDeleteDto;
import com.twat.detalks.member.dto.MemberReadDto;
import com.twat.detalks.member.dto.MemberUpdateDto;
import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.service.MemberService;
// import com.twat.detalks.security.TokenProvider;
import com.twat.detalks.oauth2.dto.CustomOAuth2User;
import com.twat.detalks.oauth2.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberAuthController {

    private final MemberService memberService;

    @Autowired
    public MemberAuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    // GET http://localhost:8080/api/member/auth
    // 회원정보조회 (로그인 유저)
    @GetMapping("/auth")
    public ResponseEntity<?> getMemberAuth(@AuthenticationPrincipal CustomOAuth2User user) {
        log.warn("user.getidx :: {}" ,user.getUserIdx());
        MemberEntity result = memberService.findByMemberId(user.getUserIdx());
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
            .qCount(result.getMemberQcount())
            .aCount(result.getMemberAcount())
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
        @AuthenticationPrincipal String idx,
        @Valid MemberUpdateDto memberUpdateDto,
        @RequestPart(required = false) MultipartFile img) {
        log.warn("memberUpdateDto {}", memberUpdateDto);
        log.warn("img file name {}", img.getOriginalFilename());
        memberService.updateMember(idx, memberUpdateDto, img);
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
    public ResponseEntity<?> restoreMemberAuth(@AuthenticationPrincipal String idx) {
        memberService.restoreMember(idx);
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
    public ResponseEntity<?> deleteMemberProfile(@AuthenticationPrincipal String idx, @Valid MemberDeleteDto memberDeleteDto) {
        memberService.deleteMember(idx, memberDeleteDto);
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
        @AuthenticationPrincipal String idx,
        @RequestParam String pwd,
        @RequestParam String changePwd) {
        memberService.changePassword(idx, pwd, changePwd);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("비밀번호 변경 성공")
                .result(true)
                .status("200")
                .build());
    }
}
