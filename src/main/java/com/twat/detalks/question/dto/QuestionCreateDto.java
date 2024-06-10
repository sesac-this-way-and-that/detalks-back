package com.twat.detalks.question.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionCreateDto {
    private String questionTitle;
    private String questionContent;
    private Long memberIdx;
    private List<String> tagNames;
}
