package com.twat.detalks.controller;

import com.twat.detalks.dto.*;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.security.TokenProvider;
import com.twat.detalks.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/member")
@Slf4j
public class MemberController {


    private TokenProvider tokenProvider;
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService, TokenProvider tokenProvider){
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }
    // POST http://localhost:8080/api/member/signup
    // 회원가입
    // 폼전송

    @PostMapping("signup")
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
    @PostMapping("signin")
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
    // 회원 조회(프로필)
    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable String id) {

        MemberEntity result = memberService.findByMemberId(id);
        MemberCreateDto data = MemberCreateDto.builder()
            .memberEmail(result.getMemberEmail())
            // .memberPwd(result.getMemberPwd())
            .memberName(result.getMemberName())
            .build();
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("프로필 조회 성공")
                .result(true)
                .data(data)
                .build());
    }

    // GET http://localhost:8080/api/member/email/{email}
    // 이메일 중복조회
    @GetMapping("email/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
        memberService.duplicateEmailCheck(email);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("사용 가능한 이메일 입니다.")
                .result(true)
                .build());
    }

    // GET http://localhost:8080/api/member/name/{name}
    // 이름 중복조회
    @GetMapping("name/{name}")
    public ResponseEntity<?> checkName(@PathVariable String name) {
        memberService.duplicateNameCheck(name);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("사용 가능한 이름입니다.")
                .result(true)
                .build());
    }

    // PATCH http://localhost:8080/api/member
    // 회원정보 수정
    // 폼전송
    // 인증필요
    @PatchMapping
    public ResponseEntity<?> updateMember(@AuthenticationPrincipal String id, @Valid MemberUpdateDto memberUpdateDto) {
        memberService.updateMember(id, memberUpdateDto);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 정보 수정 성공")
                .result(true)
                .build());
    }

    // DELETE http://localhost:8080/api/member
    // 회원탈퇴
    // 입력받을 정보 : 비밀번호, 탈퇴 사유
    // 폼전송
    @DeleteMapping
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal String id, @Valid MemberDeleteDto memberDeleteDto) {
        memberService.deleteMember(id, memberDeleteDto);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("회원 탈퇴 성공")
                .result(true)
                .build());
    }

}
