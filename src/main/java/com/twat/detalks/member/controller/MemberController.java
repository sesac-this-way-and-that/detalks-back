package com.twat.detalks.member.controller;

import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.dto.*;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.oauth2.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "비로그인 유저도 사용가능한 API")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    // POST http://localhost:8080/api/member/signup
    // 회원가입
    // 폼전송
    // 이메일(필수), 비밀번호(필수), 이름(필수)
    @PostMapping("/signup")
    @Operation(summary = "일반 회원가입")
    public ResponseEntity<?> signUp(
        @ModelAttribute @Valid MemberCreateDto memberDTO) {
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
    @Operation(summary = "일반 로그인")
    public ResponseEntity<?> signIn(
        @ModelAttribute MemberForm memberForm ) {
        MemberEntity member = memberService.getByCredentials(memberForm.getEmail(), memberForm.getPwd());
        String token = jwtUtil.createJwtNone(member, 60 * 60 * 24 * 1000L);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("로그인 성공")
                .result(true)
                .status("200")
                .token(token)
                .build());
    }

    // GET http://localhost:8080/api/member/idx/{idx}
    // 회원 정보 조회
    // 회원 IDX (필수)
    @GetMapping("/idx/{idx}")
    @Operation(summary = "회원 정보 조회")
    public ResponseEntity<?> getMember(
        @Parameter(description = "회원아이디", required = true, example = "1")
        @PathVariable String idx) {
        MemberEntity result = memberService.findByMemberId(idx);
        long questionCount = memberService.getQuestionCount(idx);
        long answerCount = memberService.getAnswerCount(idx);
        List<String> tags = memberService.getTags(idx);
        MemberReadDto data = MemberReadDto.builder()
            .name(result.getMemberName())
            .state(result.getMemberState())
            .img(result.getMemberImg())
            .summary(result.getMemberSummary())
            .about(result.getMemberAbout())
            .rep(result.getMemberRep())
            .qCount(questionCount)
            .aCount(answerCount)
            .tags(tags)
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
    @Operation(summary = "이메일 중복 조회")
    public ResponseEntity<?> checkEmail(
        @Parameter(description = "회원이메일", required = true, example = "test@test.com")
        @PathVariable String email) {
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
    @Operation(summary = "이름 중복 조회")
    public ResponseEntity<?> checkName(
        @Parameter(description = "회원이름", required = true, example = "test")
        @PathVariable String name) {
        memberService.duplicateNameCheck(name);
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("사용 가능한 이름입니다.")
                .result(true)
                .status("200")
                .build());
    }

    // PATCH http://localhost:8080/api/member/pwd
    // 비밀번호 찾기
    // 이메일(필수), 비밀번호(필수)
    @PatchMapping("/pwd")
    @Operation(summary = "비밀번호 찾기")
    public ResponseEntity<?> initPassword(
        @RequestBody FindPwdDto findPwdDto) {
        String email = findPwdDto.getEmail();
        String pwd = findPwdDto.getPwd();
        MemberEntity member = memberService.findByMemberEmail(email);
        memberService.regexPwdCheck(pwd);
        memberService.initPassword(member, pwd);

        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("비밀 번호가 변경되었습니다")
                .result(true)
                .status("200")
                .build());
    }
}