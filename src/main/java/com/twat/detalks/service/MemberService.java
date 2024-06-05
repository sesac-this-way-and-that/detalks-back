package com.twat.detalks.service;

import com.twat.detalks.dto.MemberDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 이메일 중복체크
    public void duplicateEmailCheck(String email) {
        boolean result = memberRepository.existsByMemberEmail(email);
        if (result) {
            throw new IllegalArgumentException("사용 불가능한 이메일 입니다.");
        }
    }

    // 이름 중복체크
    public void duplicateNameCheck(String name) {
        boolean result = memberRepository.existsByMemberName(name);
        if (result) {
            throw new IllegalArgumentException("사용 불가능한 이름 입니다.");
        }
    }

    // 회원가입
    public void saveMember(MemberDto memberDTO) {
        memberRepository.save(MemberEntity.builder()
            .memberEmail(memberDTO.getMemberEmail())
            .memberPwd(passwordEncoder.encode(memberDTO.getMemberPwd()))
            .memberName(memberDTO.getMemberName())
            .build());
    }

    // 회원정보조회(memberId)
    public MemberEntity findByMemberId(String id) {
        Long memberId = Long.parseLong(id);
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public MemberEntity getByCredentials(String memberEmail, String memberPwd) {
        MemberEntity result = memberRepository.findByMemberEmail(memberEmail);
        // TODO 암호화한 비밀번호 검증해서 존재하면 엔티티 반환 아니면 null 처리
        return null;
    }

}