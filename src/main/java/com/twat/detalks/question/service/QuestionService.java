package com.twat.detalks.question.service;

import com.twat.detalks.answer.dto.AnswerDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.question.dto.MemberQuestionDto;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.security.TokenProvider;
import com.twat.detalks.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenProvider tokenProvider;

    // 질문 리스트 조회
    public List<QuestionDto> getQuestions() {
        return questionRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 상세 질문 조회
    public QuestionDto getQuestionById(Long questionId) {
        QuestionDto findQuestion = questionRepository.findById(questionId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        // 조회수 업데이트
        questionRepository.updateViewCount(questionId);

        return findQuestion;
    }


    // 질문 생성
    public QuestionDto createQuestion(String memberIdx, QuestionCreateDto questionCreateDto) {
        MemberEntity member = memberRepository.findById(Long.parseLong(memberIdx))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        // MemberEntity memberCheck = tokenProvider.validateAndGetUserId()

        QuestionEntity newQuestion = QuestionEntity.builder()
                .questionTitle(questionCreateDto.getQuestionTitle())
                .questionContent(questionCreateDto.getQuestionContent())
                .members(member)
                .build();

                return convertToDTO(questionRepository.save(newQuestion));
    }

    // 질문 수정
    public QuestionDto updateQuestion(Long questionId, QuestionCreateDto questionCreateDto, String memberIdx) {
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        if (!existingQuestion.getMembers().getMemberIdx().equals(Long.parseLong(memberIdx))) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        existingQuestion.setQuestionTitle(questionCreateDto.getQuestionTitle());
        existingQuestion.setQuestionContent(questionCreateDto.getQuestionContent());

        return convertToDTO(questionRepository.save(existingQuestion));
    }

    // 질문 삭제
    public void deleteQuestion(Long questionId, String memberIdx) {
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        if (!existingQuestion.getMembers().getMemberIdx().equals(Long.parseLong(memberIdx))) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        questionRepository.delete(existingQuestion);
    }


    // 질문 entity를 dto로 변환
    private QuestionDto convertToDTO(QuestionEntity questionEntity) {
        List<AnswerDto> answerDtoList = (questionEntity.getAnswerList() != null ? questionEntity.getAnswerList() : new ArrayList<AnswerEntity>()).stream()
                .map(answer -> AnswerDto.builder()
                        .answerId(answer.getAnswerId())
                        .answerContent(answer.getAnswerContent())
                        .createdAt(answer.getCreatedAt())
                        .modifiedAt(answer.getModifiedAt())
                        .answerState(answer.getAnswerState())
                        .voteCount(answer.getVoteCount())
                        .isSelected(answer.getIsSelected())
                        .author(new MemberQuestionDto(
                                answer.getMembers().getMemberIdx(),
                                answer.getMembers().getMemberName()))
                        .build())
                .collect(Collectors.toList());

        return QuestionDto.builder()
                .questionId(questionEntity.getQuestionId())
                .questionTitle(questionEntity.getQuestionTitle())
                .questionContent(questionEntity.getQuestionContent())
                .createdAt(questionEntity.getCreatedAt())
                .modifiedAt(questionEntity.getModifiedAt())
                .viewCount(questionEntity.getViewCount())
                .voteCount(questionEntity.getVoteCount())
                .questionState(questionEntity.getQuestionState())
                .isSolved(questionEntity.getIsSolved())
                .author(new MemberQuestionDto(
                        questionEntity.getMembers().getMemberIdx(),
                        questionEntity.getMembers().getMemberName()))
                .answerList(answerDtoList)
                .build();
    }
}