package com.twat.detalks.answer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerCreateDto {
    @Schema(description = "답변 내용", example = "답변 내용입니다.")
    private String answerContent;
}
