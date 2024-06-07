package com.twat.detalks.answer.dto;

import com.twat.detalks.question.dto.MemberQuestionDto;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class AnswerDto {
    private Long answerId;
    private String answerContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean answerState;
    private int voteCount;
    private Boolean isSelected;
    private MemberQuestionDto author;
}
