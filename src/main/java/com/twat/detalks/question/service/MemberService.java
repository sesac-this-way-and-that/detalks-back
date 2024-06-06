// package com.twat.detalks.question.service;
//
//
// import detalks_test.demo.entity.MemberEntity;
// import detalks_test.demo.repository.MemberRepository;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import java.util.List;
// import java.util.Optional;
//
// @Service
// @Slf4j
// public class MemberService {
//     @Autowired
//     private MemberRepository memberRepository;
//
//     public List<MemberEntity> getMembers() {
//         return memberRepository.findAll();
//     }
//
//     public Optional<MemberEntity> getMembersByEmail(String email) {
//         return memberRepository.findByMemberEmail(email);
//     }
//
//     public Optional<MemberEntity> getMembersById(Long memberIdx) {
//         return memberRepository.findById(memberIdx);
//     }
//
// }
