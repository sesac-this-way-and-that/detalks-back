package com.twat.detalks.controller;

import com.twat.detalks.dto.*;
import com.twat.detalks.dto.member.*;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.security.TokenProvider;
import com.twat.detalks.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService, TokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    // POST http://localhost:8080/api/member/signup
    // 회원가입
    // 폼전송
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid MemberCreateDto memberDTO) {
        memberService.duplicateEmailCheck(memberDTO.getMemberEmail());
        memberService.duplicateNameCheck(memberDTO.getMemberName());
        memberService.saveMember(memberDTO);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 가입 성공")
                .result(true)
                .build());
    }

    // POST http://localhost:8080/api/member/signin
    // 로그인
    // 폼전송
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
        @RequestParam String email, @RequestParam String password) {
        MemberEntity member = memberService.getByCredentials(email, password);
        String token = tokenProvider.create(member);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("로그인 성공")
                .result(true)
                // .data(data)
                .token(token)
                .build());
    }

    // GET http://localhost:8080/api/member/{id}
    // 회원 정보 조회
    // 인증 X
    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable(required = false) String id) {
        MemberEntity result = memberService.findByMemberId(id);
        MemberReadDto data = MemberReadDto.builder()
            // .memberIdx(result.getMemberIdx())
            // .memberEmail(result.getMemberEmail())
            .memberName(result.getMemberName())
            // .memberIsDeleted(result.getMemberIsDeleted())
            // .memberReason(result.getMemberReason())
            .memberState(result.getMemberState())
            .memberImg(result.getMemberImg())
            .memberSummary(result.getMemberSummary())
            .memberAbout(result.getMemberAbout())
            .memberRep(result.getMemberRep())
            // .memberSocial(result.getMemberSocial())
            .memberQcount(result.getMemberQcount())
            .memberAcount(result.getMemberAcount())
            .memberCreated(result.getMemberCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .memberVisited(result.getMemberVisited().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            // .memberUpdated(result.getMemberUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 조회 성공")
                .result(true)
                .data(data)
                .build());
    }

    // GET http://localhost:8080/api/member/email/{email}
    // 이메일 중복조회
    @GetMapping("/email/{email}")
    public ResponseEntity<?> checkEmail(
        @PathVariable
        String email) {
        memberService.regexEmailCheck(email);
        memberService.duplicateEmailCheck(email);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("사용 가능한 이메일 입니다.")
                .result(true)
                .build());
    }

    // GET http://localhost:8080/api/member/name/{name}
    // 이름 중복조회
    @GetMapping("/name/{name}")
    public ResponseEntity<?> checkName(@PathVariable String name) {
        memberService.duplicateNameCheck(name);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("사용 가능한 이름입니다.")
                .result(true)
                .build());
    }


    // ------------------------------ 로그인 필요 ------------------------------ //


    // GET http://localhost:8080/api/member/auth
    // 회원 정보 조회 (로그인 유저)
    @GetMapping("/auth")
    public ResponseEntity<?> getMemberAuth(@AuthenticationPrincipal String id) {
        MemberEntity result = memberService.findByMemberId(id);
        MemberReadDto data = MemberReadDto.builder()
            .memberIdx(result.getMemberIdx())
            .memberEmail(result.getMemberEmail())
            .memberName(result.getMemberName())
            .memberIsDeleted(result.getMemberIsDeleted())
            .memberReason(result.getMemberReason())
            .memberState(result.getMemberState())
            .memberImg(result.getMemberImg())
            .memberSummary(result.getMemberSummary())
            .memberAbout(result.getMemberAbout())
            .memberRep(result.getMemberRep())
            .memberSocial(result.getMemberSocial())
            .memberQcount(result.getMemberQcount())
            .memberAcount(result.getMemberAcount())
            .memberCreated(result.getMemberCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .memberVisited(result.getMemberVisited().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 조회(로그인 유저) 성공")
                .result(true)
                .data(data)
                .build());
    }

    // PATCH http://localhost:8080/api/member/auth
    // 회원정보 수정
    // 폼전송
    @PatchMapping("/auth")
    public ResponseEntity<?> updateMemberAuth(@AuthenticationPrincipal String id, @Valid MemberUpdateDto memberUpdateDto) {
        memberService.updateMember(id, memberUpdateDto);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 수정 성공")
                .result(true)
                .build());
    }

    // POST http://localhost:8080/api/member/auth
    // 회원탈퇴복구
    @PostMapping("/auth")
    public ResponseEntity<?> restoreMemberAuth(@AuthenticationPrincipal String id) {
        memberService.restoreMember(id);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 복구 성공")
                .result(true)
                .build());
    }

    // DELETE http://localhost:8080/api/member/auth
    // 회원탈퇴
    // 입력받을 정보 : 비밀번호, 탈퇴 사유
    // 폼전송
    @DeleteMapping("/auth")
    public ResponseEntity<?> deleteMemberProfile(@AuthenticationPrincipal String id, @Valid MemberDeleteDto memberDeleteDto) {
        memberService.deleteMember(id, memberDeleteDto);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 탈퇴 성공")
                .result(true)
                .build());
    }

    // TODO 비밀번호 변경
    // TODO 소셜로그인
    // TODO 유저목록 검색필터링, 페이지네이션

}
