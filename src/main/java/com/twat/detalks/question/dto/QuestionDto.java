package com.twat.detalks.question.dto;

import com.twat.detalks.answer.dto.AnswerDto;
import com.twat.detalks.dto.MemberQuestionDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class QuestionDto {
    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private int viewCount;
    private int voteCount;
    private Boolean questionState;
    private Boolean isSolved;
    private MemberQuestionDto author;
    private List<AnswerDto> answerList;
}
