package com.twat.detalks.question.service;

import com.twat.detalks.dto.MemberQuestionDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberService memberService;

    // 질문 리스트 조회
    public List<QuestionDto> getQuestions() {
        return questionRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 상세 질문 조회
    public QuestionDto getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));
    }

    // 질문 리스트에 원하는 멤버 데이터 컨버트
    private QuestionDto convertToDTO(QuestionEntity question) {
        MemberQuestionDto memberQuestionDto = MemberQuestionDto.builder()
                .memberIdx(question.getMembers().getMemberIdx())
                .memberName(question.getMembers().getMemberName())
                .build();

        // List<AnswerDTO> answerList = question.getAnswerList().stream()
        //         .map(answer -> AnswerDTO.builder()
        //                 .answerId(answer.getAnswerId())
        //                 .answerContent(answer.getAnswerContent())
        //                 .build())
        //         .collect(Collectors.toList());

        return QuestionDto.builder()
                .questionId(question.getQuestionId())
                .questionTitle(question.getQuestionTitle())
                .questionContent(question.getQuestionContent())
                .createdAt(question.getCreatedAt())
                .modifiedAt(question.getModifiedAt())
                .viewCount(question.getViewCount())
                .voteCount(question.getVoteCount())
                .questionState(question.getQuestionState())
                .isSolved(question.getIsSolved())
                .author(memberQuestionDto)
                // .answerList(answerList)
                .build();
    }


    // 질문 존재 검증
    public QuestionEntity findVerifiedQuestion(Long questionId) {

        Optional<QuestionEntity> optionalQuestion =
                questionRepository.findById(questionId);
        QuestionEntity findQuestion =
                optionalQuestion.orElseThrow(() -> new NoSuchMessageException("질문을 찾을 수 없습니다."));

        return findQuestion;
    }

    // 질문 생성
    public QuestionEntity createQuestion(String memberIdx, QuestionCreateDto question) {
        // 질문 생성 시 필요한 것. title, content, member
        MemberEntity member = memberService.findByMemberId(memberIdx);

        QuestionEntity newQuestion = QuestionEntity.builder()
                .members(member)
                .questionTitle(question.getQuestionTitle())
                .questionContent(question.getQuestionContent())
                .build();

        return questionRepository.save(newQuestion);
    }

    // 질문 수정
    public QuestionEntity updateQuestion(String memberIdx, Long questionId, QuestionCreateDto question) {
    // public QuestionEntity updateQuestion(Long userid, int question_id, QuestionCreateDto question) {
        // QuestionEntity memberId = questionRepository.findByMemberId(member_idx)
        //         .orElseThrow(() -> new RuntimeException("아이디가 다릅니다."));

        // if(!question.getMemberIdx().equals(userid)) {
        //     throw new RuntimeException("수정 권한이 없습니다.");
        // }

        QuestionEntity exist = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        QuestionEntity updateQuestion = QuestionEntity.builder()
                .questionId(exist.getQuestionId())
                .questionTitle(question.getQuestionTitle())
                .questionContent(question.getQuestionContent())
                // .member_idx(userid)
                .build();

        return questionRepository.save(updateQuestion);
    }

    // 질문 삭제
    public QuestionEntity deleteQuestion(Long questionId) {
        QuestionEntity exist = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        questionRepository.delete(exist);
        return exist;
    }
}
