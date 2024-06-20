package com.twat.detalks.answer.service;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.entity.AnswerVoteEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.answer.repository.AnswerVoteRepository;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnswerVoteService {
    @Autowired
    private AnswerVoteRepository answerVoteRepository;

    @Autowired
    private AnswerRepositroy answerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Transactional
    public void addVote(Long answerId, Long memberIdx, Boolean voteState) {
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
            // 기존 투표가 있으면 voteState 수정
            // 기존 투표가 있고 현재 voteState 상태와 요청으로 온 voteState 상태가 같다면 중복 투표!
            // if (existingVote.getVoteState().equals(voteState)) {
            //     log.warn("찍히나요? {}", voteState);
            //     throw new RuntimeException("이미 투표한 답변입니다.");
            // }

            if (existingVote.getVoteState().equals(voteState)) {
                // 동일한 투표 상태인 경우 투표 취소
                answerVoteRepository.delete(existingVote);
                updateVoteCount(answerId);
                return;
            } else {
                // 다른 투표 상태인 경우 업데이트
                existingVote.setVoteState(voteState);
                answerVoteRepository.save(existingVote);
            }

            // 해당 답변의 작성자 평판 수정
            String writeMemberIdx = String.valueOf(answer.getMembers().getMemberIdx());
            // voteState 따라 action 변경
            memberService.actionMemberReputation(writeMemberIdx, voteState ? "VOTE_UP" : "VOTE_DOWN");

        }
        else if(voteState != null) {
            AnswerVoteEntity vote = AnswerVoteEntity.builder()
                    .answer(answer)
                    .member(member)
                    .voteState(voteState)
                    .build();
            answerVoteRepository.save(vote);

            // [과거 코드] -> 일단 평판 제외하고 만들었음
            // 해당 답변의 작성자 평판 수정
            // String writeMemberIdx = String.valueOf(answer.getMembers().getMemberIdx());
            // voteState 따라 action 변경
            // memberService.actionMemberReputation(writeMemberIdx, voteState ? "VOTE_UP" : "VOTE_DOWN");
        }
        updateVoteCount(answerId);
    }

    @Transactional
    public void removeVote(Long answerId, Long memberIdx) {
        AnswerVoteEntity existingVote = answerVoteRepository.findByAnswer_AnswerIdAndMember_MemberIdx(answerId, memberIdx)
                .orElseThrow(() -> new RuntimeException("투표가 존재하지 않습니다."));
        answerVoteRepository.delete(existingVote);

        updateVoteCount(answerId);
    }

    // 업데이트
    @Transactional
    private void updateVoteCount(Long answerId) {
        AnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));
        int voteSum = answerVoteRepository.findByAnswer_AnswerId(answerId).stream()
                .mapToInt(v -> v.getVoteState() ? 1 : -1)
                .sum();
        answer.setVoteCount(voteSum);
        answerRepository.save(answer);
    }

}