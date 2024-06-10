package com.twat.detalks.mypage.service;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.mypage.dto.QuestionAnswerListDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestionAnswerListService {

    private final QuestionRepository questionRepository;
    private final AnswerRepositroy answerRepository;

    // 최신순
    public List<QuestionAnswerListDto> getQuestionsAndAnswersByMemberIdOrderByCreatedAtDesc(Long memberIdx) {
        List<QuestionEntity> questions = questionRepository.findByMembers_MemberIdx(memberIdx);
        List<AnswerEntity> answers = answerRepository.findByMembers_MemberIdx(memberIdx);

        List<QuestionAnswerListDto> questionAnswerList = new ArrayList<>();

        questions.forEach(question -> questionAnswerList.add(convertToDto(question)));
        answers.forEach(answer -> questionAnswerList.add(convertToDto(answer)));

        return questionAnswerList.stream()
                .sorted(Comparator.comparing(QuestionAnswerListDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // 투표순
    public List<QuestionAnswerListDto> getQuestionsAndAnswersByMemberIdOrderByVoteCountDesc(Long memberIdx) {
        List<QuestionEntity> questions = questionRepository.findByMembers_MemberIdx(memberIdx);
        List<AnswerEntity> answers = answerRepository.findByMembers_MemberIdx(memberIdx);

        List<QuestionAnswerListDto> questionAnswerList = new ArrayList<>();

        questions.forEach(question -> questionAnswerList.add(convertToDto(question)));
        answers.forEach(answer -> questionAnswerList.add(convertToDto(answer)));

        return questionAnswerList.stream()
                .sorted(Comparator.comparingInt(QuestionAnswerListDto::getVoteCount).reversed())
                .collect(Collectors.toList());
    }

    private QuestionAnswerListDto convertToDto(QuestionEntity question) {
        return QuestionAnswerListDto.builder()
                .id(question.getQuestionId())
                .titleOrContent(question.getQuestionTitle())
                .isQuestion(true)
                .createdAt(question.getCreatedAt())
                .voteCount(question.getVoteCount())
                .isSolved(question.getIsSolved())
                .build();
    }

    private QuestionAnswerListDto convertToDto(AnswerEntity answer) {
        return QuestionAnswerListDto.builder()
                .id(answer.getAnswerId())
                .titleOrContent(answer.getAnswerContent())
                .isQuestion(false)
                .createdAt(answer.getCreatedAt())
                .voteCount(answer.getVoteCount())
                .isSelected(answer.getIsSelected())
                .build();
    }
}