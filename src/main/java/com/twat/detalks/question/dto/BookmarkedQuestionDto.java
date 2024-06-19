package com.twat.detalks.question.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkedQuestionDto {
    private Long bookmarkId;
    private Long questionId;
    private String titleOrContent;
    private int voteCount;
    private Boolean isSolved;
    private LocalDateTime createdAt;
    private Boolean isQuestion;

    public BookmarkedQuestionDto(Long bookmarkId, QuestionDto questionDto) {
        this.bookmarkId = bookmarkId;
        this.questionId = questionDto.getQuestionId();
        this.titleOrContent = questionDto.getQuestionTitle();
        this.voteCount = questionDto.getVoteCount();
        this.isSolved = questionDto.getIsSolved();
        this.createdAt = questionDto.getCreatedAt();
        this.isQuestion = true;
    }
}