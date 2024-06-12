package com.twat.detalks.question.dto;

import com.twat.detalks.answer.dto.AnswerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class QuestionDto {
    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int viewCount;
    private int voteCount;
    private Boolean questionState;
    private Boolean isSolved;
    private MemberQuestionDto author;
    private Boolean bookmarkState;
    private int answerCount; // 추가 answerList의 인덱스 총 개수
    private List<AnswerDto> answerList;
    private List<String> tagNameList;
}
