package com.twat.detalks.question.service;

import com.twat.detalks.dto.MemberQuestionDto;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.repository.MemberRepository;
import com.twat.detalks.security.TokenProvider;
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
        return questionRepository.findById(questionId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));
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



    private QuestionDto convertToDTO(QuestionEntity questionEntity) {
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
                .answerList(questionEntity.getAnswerList())
                .build();
    }

    // 질문 리스트에 원하는 멤버 데이터 컨버트
    // private QuestionDto convertToDTO(QuestionEntity question) {
    //     MemberQuestionDto memberQuestionDto = MemberQuestionDto.builder()
    //             .memberIdx(question.getMembers().getMemberIdx())
    //             .memberName(question.getMembers().getMemberName())
    //             .build();
    //
    //     // List<AnswerDTO> answerList = question.getAnswerList().stream()
    //     //         .map(answer -> AnswerDTO.builder()
    //     //                 .answerId(answer.getAnswerId())
    //     //                 .answerContent(answer.getAnswerContent())
    //     //                 .build())
    //     //         .collect(Collectors.toList());
    //
    //     return QuestionDto.builder()
    //             .questionId(question.getQuestionId())
    //             .questionTitle(question.getQuestionTitle())
    //             .questionContent(question.getQuestionContent())
    //             .createdAt(question.getCreatedAt())
    //             .modifiedAt(question.getModifiedAt())
    //             .viewCount(question.getViewCount())
    //             .voteCount(question.getVoteCount())
    //             .questionState(question.getQuestionState())
    //             .isSolved(question.getIsSolved())
    //             .author(memberQuestionDto)
    //             // .answerList(answerList)
    //             .build();
    // }
}
