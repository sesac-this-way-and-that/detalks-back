package com.twat.detalks.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionListDto {
    private Long questionId;
    private String questionTitle;
    private int voteCount;
    private Boolean isSolved;
    private LocalDateTime createdAt;
}
