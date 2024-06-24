package com.twat.detalks.email.service;

import com.twat.detalks.email.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Value("${mail.sender.email}")
    private static String senderEmail = null;

    private String createCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    // 회원가입 이메일 폼 생성
    private MimeMessage createSignUpEmailForm(String email) throws MessagingException {
        String authCode = createCode();
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[DeTalks] 회원가입을 위한 이메일 인증");  // 제목 설정
        String body = "";
        body += "<h1>" + "안녕하세요." + "</h1>";
        body += "<h1>" + "Detalks 입니다." + "</h1>";
        body += "<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>";
        body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";

        body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
        body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
        body += "<h1 style='color:blue'>" + authCode + "</h1>";
        body += "</div><br>";
        body += "<h3>" + "감사합니다." + "</h3>";
        message.setText(body, "UTF-8", "html");

        // Redis 에 해당 인증코드 인증 시간 설정
        redisUtil.setDataExpire(email, authCode, 60 * 30L);

        return message;
    }

    // 비밀번호 찾기 이메일 폼 생성
    private MimeMessage createFindPwdEmailForm(String email) throws MessagingException {
        String authCode = createCode();
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[DeTalks] 비밀번호 초기화를 위한 이메일 인증");  // 제목 설정
        String body = "";
        body += "<h1>" + "안녕하세요." + "</h1>";
        body += "<h1>" + "Detalks 입니다." + "</h1>";
        body += "<h3>" + "비밀번호 초기화를 위해 요청하신 인증 번호입니다." + "</h3><br>";
        body += "<h2>" + "아래 코드를 비밀번호 찾기로 돌아가 입력해주세요." + "</h2>";

        body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
        body += "<h2>" + "비밀번호 초기화 인증 코드입니다." + "</h2>";
        body += "<h1 style='color:blue'>" + authCode + "</h1>";
        body += "</div><br>";
        body += "<h3>" + "감사합니다." + "</h3>";
        message.setText(body, "UTF-8", "html");

        // Redis 에 해당 인증코드 인증 시간 설정
        redisUtil.setDataExpire(email, authCode, 60 * 30L);

        return message;
    }

    // 회원가입 인증코드 이메일 발송
    public void signUpSendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }
        // 이메일 폼 생성
        MimeMessage emailForm = createSignUpEmailForm(toEmail);
        // 이메일 발송
        javaMailSender.send(emailForm);
    }

    // 비밀번호 찾기 인증코드 이메일 발송
    public void findPwdSendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }
        // 이메일 폼 생성
        MimeMessage emailForm = createFindPwdEmailForm(toEmail);
        // 이메일 발송
        javaMailSender.send(emailForm);
    }

    // 인증코드 검증
    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
    }
}