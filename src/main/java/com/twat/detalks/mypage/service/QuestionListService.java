package com.twat.detalks.mypage.service;

import com.twat.detalks.mypage.dto.QuestionListDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionListService {
    @Autowired
    private QuestionRepository questionRepository;

    public Page<QuestionListDto> getQuestionsByMemberId(Long memberIdx, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<QuestionEntity> questionsPage = questionRepository.findByMembers_MemberIdxAndIsSolvedFalse(memberIdx, pageable);
        return questionsPage.map(this::convertToDto);
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
