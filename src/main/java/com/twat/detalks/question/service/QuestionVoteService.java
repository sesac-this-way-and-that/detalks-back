package com.twat.detalks.question.service;

import com.twat.detalks.answer.entity.AnswerVoteEntity;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.question.entity.QuestionVoteEntity;
import com.twat.detalks.question.repository.QuestionVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class QuestionVoteService {
    @Autowired
    private QuestionVoteRepository voteRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

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
        Optional<QuestionVoteEntity> existingVote = voteRepository.findByQuestions_QuestionIdAndMembers_MemberIdx(questionId, memberIdx);
        QuestionVoteEntity vote;

        if (existingVote != null) {
            vote = existingVote.get();
            // 기존 투표가 있으면 voteState 수정
            if (voteState == null) {
                voteRepository.delete(vote);
            } else {
                vote.setVoteState(voteState);
                voteRepository.save(vote);
            }
        } else {
            if (voteState != null) {
                vote = QuestionVoteEntity.builder()
                        .questions(question)
                        .members(member)
                        .voteState(voteState)
                        .build();
                voteRepository.save(vote);
            }
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
