package com.twat.detalks.member.controller;

import com.twat.detalks.member.dto.VerifyEmailDto;
import com.twat.detalks.member.service.EmailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@Slf4j
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public String mailConfirm(@RequestBody @Valid VerifyEmailDto verifyEmailDto) {
        int num = emailService.sendEmail(verifyEmailDto.getEmail());
        return "인증 코드 :: " + num;
    }
}
