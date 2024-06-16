package com.twat.detalks.question.service;

import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.question.entity.QuestionVoteEntity;
import com.twat.detalks.question.repository.QuestionVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionVoteService {
    @Autowired
    private QuestionVoteRepository voteRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    public void vote(Long questionId, Long memberIdx, Boolean voteState) {
        QuestionEntity question = questionRepository.findById(questionId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        MemberEntity member = memberRepository.findById(memberIdx)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        // 자신의 게시물에 투표할 수 없음
        if (question.getMembers().getMemberIdx().equals(memberIdx)) {
            throw new RuntimeException("자신의 질문에는 투표할 수 없습니다.");
        }

        // 기존 투표 여부 확인
        QuestionVoteEntity existingVote = voteRepository.findByQuestions_QuestionIdAndMembers_MemberIdx(questionId, memberIdx).orElse(null);

        if (existingVote != null) {
            // 기존 투표가 있으면 voteState 수정
            // 기존 투표가 있고 현재 voteState 상태와 요청으로 온 voteState 상태가 같다면 중복 투표!
            if(existingVote.getVoteState().equals(voteState)){
                throw new IllegalArgumentException("이미 투표한 질문 입니다.");
            }
            // 여기로 넘어온다면 중복 투표 X
            existingVote.setVoteState(voteState);

            // 해당 질문의 작성자 평판 수정
            String writeMemberIdx = String.valueOf(question.getMembers().getMemberIdx());
            // voteState 따라 action 변경
            memberService.actionMemberReputation(writeMemberIdx, voteState ? "VOTE_UP" : "VOTE_DOWN");

            voteRepository.saveAndFlush(existingVote);
        } else {
            // 기존 투표가 없다면 새로 추가
            QuestionVoteEntity newVote = QuestionVoteEntity.builder()
                .questions(question)
                .members(member)
                .voteState(voteState)
                .build();
            voteRepository.saveAndFlush(newVote);

            // 해당 질문의 작성자 평판 수정
            String writeMemberIdx = String.valueOf(question.getMembers().getMemberIdx());
            // voteState 따라 action 변경
            memberService.actionMemberReputation(writeMemberIdx, voteState ? "VOTE_UP" : "VOTE_DOWN");
        }

        // 투표 수 업데이트
        int voteSum = voteRepository.findByQuestions_QuestionId(questionId).stream()
            .mapToInt(v -> v.getVoteState() ? 1 : -1)
            .sum();

        question.setVoteCount(voteSum);
        questionRepository.save(question);
    }

    @Transactional
    public void removeVote(Long questionId, Long memberIdx) {
        voteRepository.deleteByQuestions_QuestionIdAndMembers_MemberIdx(questionId, memberIdx);

        // 답변에 대한 총 투표 수 업데이트
        int voteSum = voteRepository.findByQuestions_QuestionId(questionId).stream()
            .mapToInt(v -> v.getVoteState() ? 1 : -1)
            .sum();

        QuestionEntity question = questionRepository.findById(questionId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));
        question.setVoteCount(voteSum);
        questionRepository.save(question);
    }
}
