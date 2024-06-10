package com.twat.detalks.mypage.service;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.mypage.dto.AnswerListDto;
import com.twat.detalks.mypage.dto.QuestionListDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerListService {
    @Autowired
    private AnswerRepositroy answerRepositroy;

    public List<AnswerListDto> getAnswersByMemberId(Long memberIdx) {
        List<AnswerEntity> answers = answerRepositroy.findByMembers_MemberIdx(memberIdx);
        return answers.stream().map(this::convertToDto).collect(Collectors.toList());
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
