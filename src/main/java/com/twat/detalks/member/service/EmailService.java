package com.twat.detalks.member.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;  // 의존성 주입을 통해 필요한 객체를 가져옴

    @Value("${mail.sender.email}")
    private String senderEmail;  // application.properties에서 값 주입

    private static int number;  // 랜덤 인증 코드

    // 랜덤 인증 코드 생성
    public static void createNumber() {
        number = (int)(Math.random() * (900000)) + 100000; // 100000부터 999999 사이의 난수 생성
    }

    // 회원 가입용 메일폼
    public MimeMessage createMail(String email){
        createNumber();  // 인증 코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject("[DeTalks] 회원가입을 위한 이메일 인증");  // 제목 설정
            String body = "";
            body += "<h1>" + "안녕하세요." + "</h1>";
            body += "<h1>" + "Detalks 입니다." + "</h1>";
            body += "<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>";
            body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";

            body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
            body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
            body += "<h1 style='color:blue'>" + number + "</h1>";
            body += "</div><br>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create email message", e);
        }

        return message;
    }

    // 비밀 번호 찾기용 메일폼
    public MimeMessage findPasswordMail(String email){
        createNumber();  // 인증 코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject("[DeTalks] 비밀번호 재설정을 위한 이메일 인증");  // 제목 설정
            String body = "";
            body += "<h1>" + "안녕하세요." + "</h1>";
            body += "<h1>" + "Detalks 입니다." + "</h1>";
            body += "<h3>" + "비밀번호 재설정을 위해 요청하신 인증 번호입니다." + "</h3><br>";
            body += "<h2>" + "아래 코드를 비밀번호 재설정 창으로 돌아가 입력해주세요." + "</h2>";

            body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
            body += "<h2>" + "인증 코드입니다." + "</h2>";
            body += "<h1 style='color:blue'>" + number + "</h1>";
            body += "</div><br>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create email message", e);
        }

        return message;
    }

    // 실제 메일 전송
    public int sendEmail(String email) {
        // 메일 전송에 필요한 정보 설정
        MimeMessage message = createMail(email);
        // 실제 메일 전송
        javaMailSender.send(message);
        // 인증 코드 반환
        return number;
    }

    public int sendFindMail(String email){
        MimeMessage message = findPasswordMail(email);
        javaMailSender.send(message);
        return number;
    }
}
