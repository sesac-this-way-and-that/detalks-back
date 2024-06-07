package com.twat.detalks.answer.service;

import com.twat.detalks.answer.dto.AnswerCreateDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AnswerService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepositroy answerRepositroy;

    // 질문에 대한 답변 생성
    public AnswerEntity createAnswer(String memberIdx, Long questionId, AnswerCreateDto answerCreateDto) {
        MemberEntity member = memberRepository.findById(Long.parseLong(memberIdx))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("질문이 존재하지 않습니다."));

        if (question.getMembers().getMemberIdx().equals(member.getMemberIdx())) {
            throw new RuntimeException("자신의 질문에는 답변할 수 없습니다.");
        }

        // 사용자가 이미 해당 질문에 대해 답변을 작성했는지 확인
        if (answerRepositroy.findByQuestionsAndMembers(question, member).isPresent()) {
            throw new RuntimeException("해당 질문에 이미 답변을 작성했습니다.");
        }

        AnswerEntity newAnswer = AnswerEntity.builder()
                .answerContent(answerCreateDto.getAnswerContent())
                .members(member)
                .questions(question)
                .build();

        return answerRepositroy.save(newAnswer);
    }

    // 답변 수정
    public AnswerEntity updateAnswer(String memberId, Long answerId, AnswerCreateDto answerCreateDto) {
        AnswerEntity existingAnswer = answerRepositroy.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));

        if (!existingAnswer.getMembers().getMemberIdx().equals(Long.parseLong(memberId))) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        existingAnswer.setAnswerContent(answerCreateDto.getAnswerContent());

        return answerRepositroy.save(existingAnswer);
    }

    // 답변 삭제
    public void deleteAnswer(String memberId, Long answerId) {
        AnswerEntity existingAnswer = answerRepositroy.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));

        if (!existingAnswer.getMembers().getMemberIdx().equals(Long.parseLong(memberId))) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        answerRepositroy.delete(existingAnswer);
    }
}
