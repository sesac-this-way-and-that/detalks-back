package com.twat.detalks.mypage.service;

import com.twat.detalks.mypage.dto.QuestionListDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionListService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionListDto> getQuestionsByMemberId(Long memberIdx) {
        List<QuestionEntity> questions = questionRepository.findByMembers_MemberIdx(memberIdx);
        return questions.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private QuestionListDto convertToDto(QuestionEntity question) {
        return QuestionListDto.builder()
                .questionId(question.getQuestionId())
                .questionTitle(question.getQuestionTitle())
                .voteCount(question.getVoteCount())
                .isSolved(question.getIsSolved())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
