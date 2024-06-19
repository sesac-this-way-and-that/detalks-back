package com.twat.detalks.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionCreateDto {

    @Schema(description = "질문 제목", example = "질문 작성 테스트")
    private String questionTitle;

    @Schema(description = "질문 내용", example = "질문 내용입니다.")
    private String questionContent;

    @Schema(description = "질문 제목", example = "회원 아이디")
    private Long memberIdx;

    @Schema(description = "태그 목록", example = "["+"java,"+" spring,"+" react"+"]")
    private List<String> tagNames;

    @Schema(description = "현상금", example = "0")
    private Integer questionRep;
}
