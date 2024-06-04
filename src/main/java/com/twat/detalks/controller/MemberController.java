package com.twat.detalks.controller;

import com.twat.detalks.dto.MemberDto;
import com.twat.detalks.dto.ResDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    // GET http://localhost:8080/member
    // 전체 회원 목록 조회
    // 테스트 용
    @GetMapping
    public ResponseEntity<?> getMemberList() {
        List<MemberEntity> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    // GET http://localhost:8080/member/id
    // 회원 조회(프로필)
    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable String id) {
        try {
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

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                ResDto.builder()
                    .msg(e.getMessage())
                    .result(false)
                    .build());
        }
    }

    // POST http://localhost:8080/member
    // 회원가입
    @PostMapping
    public ResponseEntity<?> saveMember(MemberDto memberDTO) {
        try {
            memberService.saveMember(memberDTO);
            return ResponseEntity.ok().body(
                ResDto.builder()
                    .msg("회원 가입 성공")
                    .result(true)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                ResDto.builder()
                    .msg(e.getMessage())
                    .result(false)
                    .build());
        }
    }


}
