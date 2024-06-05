package com.twat.detalks.controller;

import com.twat.detalks.dto.MemberDto;
import com.twat.detalks.dto.ResDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    // POST http://localhost:8080/api/member/signup
    // 회원가입
    // 폼전송
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(MemberDto memberDTO) {
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
    public ResponseEntity<?> signIn() {
        // TODO
        return null;
    }

    // GET http://localhost:8080/api/member/{id}
    // 회원 조회(프로필)
    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable String id) {

        MemberEntity result = memberService.findByMemberId(id);
        MemberDto data = MemberDto.builder()
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
}
