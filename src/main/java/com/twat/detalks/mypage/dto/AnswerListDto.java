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
public class AnswerListDto {
    private Long answerId;
    private String answerContent;
    private int voteCount;
    private Boolean isSelected;
    private LocalDateTime createdAt;
}