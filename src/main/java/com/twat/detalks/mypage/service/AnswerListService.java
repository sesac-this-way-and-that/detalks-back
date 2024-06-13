package com.twat.detalks.mypage.service;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.mypage.dto.AnswerListDto;
import com.twat.detalks.mypage.dto.QuestionListDto;
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
public class AnswerListService {
    @Autowired
    private AnswerRepositroy answerRepositroy;

    public Page<AnswerListDto> getAnswersByMemberId(Long memberIdx, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<AnswerEntity> answersPage = answerRepositroy.findByMembers_MemberIdxAndIsSelectedFalse(memberIdx, pageable);
        return answersPage.map(this::convertToDto);
    }

    private AnswerListDto convertToDto(AnswerEntity answer) {
        return AnswerListDto.builder()
                .answerId(answer.getAnswerId())
                .answerContent(answer.getAnswerContent())
                .voteCount(answer.getVoteCount())
                .isSelected(answer.getIsSelected())
                .createdAt(answer.getCreatedAt())
                .build();
    }
}
