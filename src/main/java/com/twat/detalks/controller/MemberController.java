package com.twat.detalks.controller;

import com.twat.detalks.dto.MemberDto;
import com.twat.detalks.dto.ResDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // GET http://localhost:8080/api/member/list
    // 전체 회원 목록 조회
    // 테스트 용
    // @GetMapping("/list")
    // public ResponseEntity<?> getMemberList() {
    //     List<MemberEntity> members = memberService.findAll();
    //     return ResponseEntity.ok(members);
    // }

    // GET http://localhost:8080/api/member/{id}
    // 회원 프로필 조회(id값으로)
    @GetMapping("/{id}")
    public ResponseEntity<?> findProfile(@PathVariable String id) {
            MemberEntity result = memberService.findByMemberId(id);
            MemberDto data = MemberDto.builder()
                .memberEmail(result.getMemberEmail())
                // .memberPwd(result.getMemberPwd())
                .memberName(result.getMemberName())
                .build();

            return ResponseEntity.ok().body(
                ResDto.builder()
                    .msg("회원 조회(id) 성공")
                    .result(true)
                    .data(data)
                    .build());
    }

    // GET http://localhost:8080/api/member/email/{email}
    // 회원가입시 이메일 중복조회
    @GetMapping("email/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
            memberService.duplicateEmailCheck(email);
            return ResponseEntity.ok().body(
                ResDto.builder()
                    .msg("이메일 사용 가능")
                    .result(true)
                    .build());

    }

    // GET http://localhost:8080/api/member/name/{name}
    // 회원가입시 이름 중복조회
    @GetMapping("name/{name}")
    public ResponseEntity<?> checkName(@PathVariable String name) {
            memberService.duplicateNameCheck(name);
            return ResponseEntity.ok().body(
                ResDto.builder()
                    .msg("이름 사용 가능")
                    .result(true)
                    .build());
    }

    // POST http://localhost:8080/api/member/signup
    // 회원가입
    @PostMapping("signup")
    public ResponseEntity<?> signUp(@Valid MemberDto memberDTO) {
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
    @PostMapping("signin")
    public ResponseEntity<?> signIn(MemberDto memberDto){
        memberService.duplicateEmailCheck(memberDto.getMemberEmail());
        return null;
    }


}
