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

import java.util.List;

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

        // 본인 질문에 답변했을 경우
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

    // 답변 채택
    @Transactional
    public void selectAnswer(Long questionId, Long answerId, Long memberIdx) {
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        if (!question.getMembers().getMemberIdx().equals(memberIdx)) {
            throw new RuntimeException("채택 권한이 없습니다.");
        }

        if (question.getIsSolved()) {
            throw new RuntimeException("이미 채택된 답변이 있습니다.");
        }

        AnswerEntity answer = answerRepositroy.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));

        if (!answer.getQuestions().getQuestionId().equals(questionId)) {
            throw new RuntimeException("해당 질문의 답변이 아닙니다.");
        }

        MemberEntity questionAuthor = question.getMembers();
        MemberEntity answerAuthor = answer.getMembers();

        int questionRep = question.getQuestionRep();

        // 질문 작성자의 평판 점수를 답변 작성자에게 이전
        MemberEntity updatedAnswerAuthor = answerAuthor.toBuilder()
                .memberRep(answerAuthor.getMemberRep() + questionRep)
                .build();
        memberRepository.save(updatedAnswerAuthor);

        QuestionEntity updatedQuestion = question.toBuilder()
                .isSolved(true)
                .build();
        AnswerEntity updatedAnswer = answer.toBuilder()
                .isSelected(true)
                .build();

        questionRepository.save(updatedQuestion);
        answerRepositroy.save(updatedAnswer);
    }

    // 회원 별로 답변 리스트 조회
    public List<AnswerEntity> getAnswersByMember(Long memberIdx) {
        return answerRepositroy.findByMembers_MemberIdx(memberIdx);
    }
}
