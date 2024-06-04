package com.twat.detalks.service;

import com.twat.detalks.dto.MemberDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;


    // 전체회원목록 조회
    public List<MemberEntity> findAll() {
        return memberRepository.findAll();
    }

    // 회원가입
    public MemberEntity saveMember(MemberDto memberDTO) {
        // 이메일 중복 여부 체크
        memberRepository.findByMemberEmail(memberDTO.getMemberEmail())
            .ifPresent(member -> {
                throw new RuntimeException("이미 사용중인 이메일입니다.");
            });
        return memberRepository.save(MemberEntity.builder()
            .memberEmail(memberDTO.getMemberEmail())
            .memberPwd(memberDTO.getMemberPwd())
            .memberName(memberDTO.getMemberName())
            .build());
    }

    // 회원정보조회(memberId)
    public MemberEntity findByMemberId(String id) {
        Long memberId = Long.parseLong(id);
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("없는 회원입니다."));
    }
}