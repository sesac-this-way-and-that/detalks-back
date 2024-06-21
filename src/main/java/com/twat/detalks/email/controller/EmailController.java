package com.twat.detalks.email.controller;

import com.twat.detalks.email.dto.EmailDto;
import com.twat.detalks.email.service.EmailService;
import com.twat.detalks.member.dto.ResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Tag(name = "이메일 인증 API", description = "회원가입, 비밀번호 찾기시 사용")
public class EmailController {
    private final EmailService emailService;

    // 회원가입 인증코드 메일 발송
    @PostMapping("/send")
    @Operation(summary = "회원가입 인증코드 메일 발송")
    public ResponseEntity<?> signUpMailSend(@RequestBody EmailDto emailDto) throws MessagingException {
        emailService.signUpSendEmail(emailDto.getEmail());
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("인증 코드가 발송되었습니다.")
                .result(true)
                .build());
    }

    // 인증코드 검증
    @PostMapping("/verify")
    @Operation(summary = "인증 코드 검증", description = "인증 코드 필수!")
    public ResponseEntity<ResDto> verify(@RequestBody EmailDto emailDto) {
        boolean isVerify = emailService.verifyEmailCode(emailDto.getEmail(), emailDto.getCode());
        if (isVerify) {
            return ResponseEntity.ok().body(
                ResDto.builder()
                    .msg("인증이 완료되었습니다.")
                    .result(true)
                    .build());
        } else {
            return ResponseEntity.badRequest().body(
                ResDto.builder()
                    .msg("인증에 실패하셨습니다.")
                    .result(false)
                    .build());
        }
    }

    // 비밀번호 찾기 인증코드 메일 발송
    @PostMapping("/password/send")
    @Operation(summary = "비밀번호 찾기 인증코드 메일 발송")
    public ResponseEntity<?> findPwdMailSend(@RequestBody EmailDto emailDto) throws MessagingException {
        emailService.findPwdSendEmail(emailDto.getEmail());
        return ResponseEntity.ok().body(
            ResDto.builder()
                .msg("인증 코드가 발송되었습니다.")
                .result(true)
                .build());
    }


}