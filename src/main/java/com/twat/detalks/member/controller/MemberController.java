package com.twat.detalks.member.controller;

import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.dto.*;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.oauth2.jwt.JWTUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, JWTUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    // POST http://localhost:8080/api/member/signup
    // 회원가입
    // 폼전송
    // 이메일(필수), 비밀번호(필수), 이름(필수)
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid MemberCreateDto memberDTO) {
        memberService.duplicateEmailCheck(memberDTO.getEmail());
        memberService.duplicateNameCheck(memberDTO.getName());
        memberService.saveMember(memberDTO);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 가입 성공")
                .result(true)
                .status("200")
                .build());
    }

    // POST http://localhost:8080/api/member/signin
    // 로그인
    // 폼전송
    // 이메일(필수), 비밀번호(필수)
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
        @RequestParam String email, @RequestParam String pwd) {
        MemberEntity member = memberService.getByCredentials(email, pwd);
        String token = jwtUtil.createJwtNone(member,60*60*24*1000L);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("로그인 성공")
                .result(true)
                .status("200")
                .token(token)
                .build());
    }

    // GET http://localhost:8080/api/member/{id}
    // 회원 정보 조회
    // 회원 IDX (필수)
    @GetMapping("/{idx}")
    public ResponseEntity<?> getMember(@PathVariable String idx) {
        MemberEntity result = memberService.findByMemberId(idx);
        MemberReadDto data = MemberReadDto.builder()
            .name(result.getMemberName())
            .state(result.getMemberState())
            .img(result.getMemberImg())
            .summary(result.getMemberSummary())
            .about(result.getMemberAbout())
            .rep(result.getMemberRep())
            .qCount(result.getMemberQcount())
            .aCount(result.getMemberAcount())
            .created(result.getMemberCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .visited(result.getMemberVisited().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 조회 성공")
                .result(true)
                .status("200")
                .data(data)
                .build());
    }

    // GET http://localhost:8080/api/member/email/{email}
    // 이메일 중복조회
    // 이메일(필수)
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
                .status("200")
                .build());
    }

    // GET http://localhost:8080/api/member/name/{name}
    // 이름 중복조회
    // 이름(필수)
    @GetMapping("/name/{name}")
    public ResponseEntity<?> checkName(@PathVariable String name) {
        memberService.duplicateNameCheck(name);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("사용 가능한 이름입니다.")
                .result(true)
                .status("200")
                .build());
    }
}
