// package com.twat.detalks.member.controller;
//
// import com.twat.detalks.member.dto.VerifyEmailDto;
// import com.twat.detalks.member.service.EmailService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// @RequestMapping("/api/email")
// @RequiredArgsConstructor
// @Tag(name = "이메일 인증 API", description = "")
// @Slf4j
// public class EmailController {
//
//     private final EmailService emailService;
//
//     @PostMapping
//     @Operation(summary = "이메일 인증메일 발송  (회원가입)")
//     public String mailConfirm(@RequestBody @Valid VerifyEmailDto verifyEmailDto) {
//         int num = emailService.sendEmail(verifyEmailDto.getEmail());
//         return "인증 코드 :: " + num;
//     }
//
//     @PostMapping("/password")
//     @Operation(summary = "이메일 인증메일 발송  (비밀번호 찾기)")
//     public String passwordConfirm(@RequestBody @Valid VerifyEmailDto verifyEmailDto) {
//         int num = emailService.sendFindMail(verifyEmailDto.getEmail());
//         return "인증 코드 :: " + num;
//     }
//
// }
