package com.twat.detalks.answer.dto;

import com.twat.detalks.dto.MemberQuestionDto;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class AnswerDto {
    private Long answerId;
    private String answerContent;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private Boolean answerState;
    private int voteCount;
    private Boolean isSelected;
    private MemberQuestionDto author;
}
