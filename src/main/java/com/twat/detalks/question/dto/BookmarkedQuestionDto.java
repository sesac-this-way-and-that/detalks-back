package com.twat.detalks.question.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkedQuestionDto {
    private Long bookmarkId;
    private QuestionDto question;
}