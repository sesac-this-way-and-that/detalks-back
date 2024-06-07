package com.twat.detalks.member.service;

import com.twat.detalks.member.dto.MemberDeleteDto;
import com.twat.detalks.member.dto.MemberCreateDto;
import com.twat.detalks.member.dto.MemberUpdateDto;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.member.vo.Social;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
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
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);


    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 이메일 유효성 검사
    public void regexEmailCheck(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
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

    // 비밀번호 검증
    public void checkPassword(final String password, final String memberPwd) {
        boolean matches = passwordEncoder.matches(password, memberPwd);
        if (!matches) throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
    }

    // 비밀번호 유효성 검사
    public void regexPwdCheck(final String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
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

    // 회원정보조회(id)
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
        // 비밀번호 검증
        checkPassword(memberPwd, loginMember.getMemberPwd());
        // 계정 상태 확인
        if(!loginMember.getMemberState()){
            throw new IllegalArgumentException("비활성화된 계정입니다.");
        }
        // 방문일자 수정 후 반환
        MemberEntity updateVisited = loginMember.toBuilder()
            .memberVisited(LocalDateTime.now())
            .build();
        memberRepository.save(updateVisited);
        return updateVisited;
    }

    // 회원 정보 수정
    public void updateMember(final String id, final MemberUpdateDto memberUpdateDto) {
        // 아이디로 회원 조회
        MemberEntity prevMember = findByMemberId(id);
        // 변경 가능 정보
        // 이름(필수), 비밀번호(필수), 프로필 이미지 경로(필수), 한줄소개, 자기소개
        // 수동 변경
        // 정보 수정 날짜
        MemberEntity updateMember = prevMember.toBuilder()
            .memberName(memberUpdateDto.getMemberName())
            .memberImg(memberUpdateDto.getMemberImg())
            .memberSummary(memberUpdateDto.getMemberSummary())
            .memberAbout(memberUpdateDto.getMemberAbout())
            .memberUpdated(LocalDateTime.now())
            .build();

        memberRepository.save(updateMember);
    }

    // 회원 탈퇴
    public void deleteMember(final String id, final MemberDeleteDto memberDeleteDto) {
        // 아이디로 회원 조회
        MemberEntity existMember = findByMemberId(id);
        // 비밀번호 검증
        checkPassword(memberDeleteDto.getMemberPwd(), existMember.getMemberPwd());
        // 논리 삭제
        MemberEntity deleteMember = existMember.toBuilder()
            .memberIsDeleted(true)
            .memberDeleted(LocalDateTime.now())
            .memberReason(memberDeleteDto.getMemberReason())
            .build();

        memberRepository.save(deleteMember);
    }

    // 회원 탈퇴 복구
    public void restoreMember(final String id) {
        // 아이디로 회원 조회
        MemberEntity existMember = findByMemberId(id);
        // 복구
        MemberEntity restoreMember = existMember.toBuilder()
            .memberIsDeleted(false)
            .memberDeleted(null)
            .memberReason(null)
            .build();
        memberRepository.save(restoreMember);
    }

    // 비밀번호 변경
    public void changePassword(final String id, final String currentPwd, final String changePwd) {
        // 아이디로 회원 조회
        MemberEntity prevMember = findByMemberId(id);
        // 소셜 회원 여부 조회
        if(prevMember.getMemberSocial() != Social.NONE) {
            throw new IllegalArgumentException("소셜 로그인 유저는 비밀번호를 변경할 수 없습니다.");
        }
        // 기존 비밀번호 검증
        checkPassword(currentPwd, prevMember.getMemberPwd());
        // 새 비밀번호 유효성 검사
        regexPwdCheck(changePwd);
        // 새 비밀번호로 변경
        MemberEntity updateMember = prevMember.toBuilder()
            .memberPwd(passwordEncoder.encode(changePwd))
            .build();

        memberRepository.save(updateMember);
    }



    // 비밀번호 찾기
    // 난수생성 ?
    // 이메일 인증
    public void findPassword(final String id) {
    }
}