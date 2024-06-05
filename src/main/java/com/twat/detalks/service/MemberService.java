package com.twat.detalks.service;

import com.twat.detalks.dto.MemberDeleteDto;
import com.twat.detalks.dto.MemberCreateDto;
import com.twat.detalks.dto.MemberUpdateDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {

    private MemberRepository memberRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 이메일 중복체크
    public void duplicateEmailCheck(final String email) {
        boolean result = memberRepository.existsByMemberEmail(email);
        if (result) {
            throw new IllegalArgumentException("사용 불가능한 이메일 입니다.");
        }
    }

    // 이름 중복체크
    public void duplicateNameCheck(final String name) {
        boolean result = memberRepository.existsByMemberName(name);
        if (result) {
            throw new IllegalArgumentException("사용 불가능한 이름 입니다.");
        }
    }

    // 회원가입
    public void saveMember(final MemberCreateDto memberDTO) {
        memberRepository.save(MemberEntity.builder()
            .memberEmail(memberDTO.getMemberEmail())
            .memberPwd(passwordEncoder.encode(memberDTO.getMemberPwd()))
            .memberName(memberDTO.getMemberName())
            .build());
    }

    // 회원정보조회(memberId)
    public MemberEntity findByMemberId(final String id) {
        Long memberId = Long.parseLong(id);
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // 로그인
    public MemberEntity getByCredentials(final String memberEmail, final String memberPwd) {
        // 암호화한 비밀번호 검증해서 존재하면 엔티티 반환 아니면 예외처리
        // 이메일 조회
        MemberEntity loginMember = memberRepository.findByMemberEmail(memberEmail)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        // 비밀번호 검사
        boolean matches = passwordEncoder.matches(memberPwd, loginMember.getMemberPwd());
        // 예외처리
        if(!matches) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        return loginMember;
    }

    // 회원 정보 수정
    public void updateMember(final String id, final MemberUpdateDto memberUpdateDto) {
        // 아이디로 회원 조회
        MemberEntity prevMember = findByMemberId(id);
        // 새로운 엔티티 생성
        MemberEntity updateMember = MemberEntity.builder()
            .memberIdx(prevMember.getMemberIdx()) // 기존 값
            .memberEmail(prevMember.getMemberEmail()) // 수정 불가
            .memberPwd(passwordEncoder.encode(memberUpdateDto.getMemberPwd())) // 수정 가능, 암호화
            .memberName(memberUpdateDto.getMemberName())
            .build();
        memberRepository.save(updateMember);
    }

    // 회원 탈퇴
    public void deleteMember(final String id, final MemberDeleteDto memberDeleteDto) {

        // 토큰값으로 로그인 유무 판별
        MemberEntity existMember = findByMemberId(id);
        // 비밀번호 검증
        boolean matches = passwordEncoder.matches(memberDeleteDto.getMemberPwd(), existMember.getMemberPwd());
        if(!matches) throw new IllegalArgumentException("비밀번호가 틀렸습니다.");

        // 컬럼 비활성화
        MemberEntity deleteMember = MemberEntity.builder()
            .memberIdx(existMember.getMemberIdx())
            .memberEmail(existMember.getMemberEmail())
            .memberPwd(existMember.getMemberPwd())
            .memberName(existMember.getMemberName())
            .memberIsDeleted(true)
            .memberReason(memberDeleteDto.getMemberReason())
            .build();
        memberRepository.save(deleteMember);
    }
}