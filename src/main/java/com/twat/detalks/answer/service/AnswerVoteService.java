package com.twat.detalks.answer.service;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.entity.AnswerVoteEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.answer.repository.AnswerVoteRepository;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerVoteService {
    @Autowired
    private AnswerVoteRepository answerVoteRepository;

    @Autowired
    private AnswerRepositroy answerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void voteAnswer(Long answerId, Long memberIdx, boolean voteState) {
        AnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));
        MemberEntity member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (answer.getMembers().getMemberIdx().equals(memberIdx)) {
            throw new RuntimeException("본인의 답변에는 투표할 수 없습니다.");
        }

        // 투표 존재 여부 확인
        AnswerVoteEntity existingVote = answerVoteRepository.findByAnswer_AnswerIdAndMember_MemberIdx(answerId, memberIdx).orElse(null);
        if (existingVote != null) {
            existingVote.setVoteState(voteState);
            answerVoteRepository.save(existingVote);
        } else {
            AnswerVoteEntity vote = AnswerVoteEntity.builder()
                    .answer(answer)
                    .member(member)
                    .voteState(voteState)
                    .build();
            answerVoteRepository.save(vote);
        }

        // 답변에 대한 총 투표 수 업데이트
        int voteSum = answerVoteRepository.findByAnswer_AnswerId(answerId).stream()
                .mapToInt(v -> v.getVoteState() ? 1 : -1)
                .sum();

        answer.setVoteCount(voteSum);
        answerRepository.save(answer);
    }

    @Transactional
    public void removeVote(Long answerId, Long memberIdx) {
        answerVoteRepository.deleteByAnswer_AnswerIdAndMember_MemberIdx(answerId, memberIdx);

        // 답변에 대한 총 투표 수 업데이트
        int voteSum = answerVoteRepository.findByAnswer_AnswerId(answerId).stream()
                .mapToInt(v -> v.getVoteState() ? 1 : -1)
                .sum();

        AnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));
        answer.setVoteCount(voteSum);
        answerRepository.save(answer);
    }
}
