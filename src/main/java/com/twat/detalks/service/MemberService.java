package com.twat.detalks.service;

import com.twat.detalks.dto.member.MemberDeleteDto;
import com.twat.detalks.dto.member.MemberCreateDto;
import com.twat.detalks.dto.member.MemberUpdateDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 이메일 유효성 검사
    public void regexEmailCheck(final String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()){
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
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
        if (!matches) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        // TODO 활성여부컬럼 체크 후 예외처리

        return loginMember;
    }

    // 회원 정보 수정
    public void updateMember(final String id, final MemberUpdateDto memberUpdateDto) {
        // 아이디로 회원 조회
        MemberEntity prevMember = findByMemberId(id);
        // 새로운 엔티티 생성
        MemberEntity updateMember = prevMember.toBuilder()
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
        if (!matches) throw new IllegalArgumentException("비밀번호가 틀렸습니다.");

        MemberEntity deleteMember = existMember.toBuilder()
            .memberIsDeleted(true)
            .memberDeleted(LocalDateTime.now())
            .memberReason(memberDeleteDto.getMemberReason())
            .build();
        // 컬럼 비활성화
        memberRepository.save(deleteMember);
    }

    //회원 탈퇴 복구
    public void restoreMember(final String id) {

        // 토큰값으로 로그인 유무 판별
        MemberEntity existMember = findByMemberId(id);

        MemberEntity restoreMember = existMember.toBuilder()
            .memberIsDeleted(false)
            .memberDeleted(null)
            .memberReason(null)
            .build();
        // 컬럼 비활성화
        memberRepository.save(restoreMember);
    }
}