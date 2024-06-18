package com.twat.detalks.answer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerVoteDto {
    private Long voteId;
    private Boolean voteState;
    private String memberIdx;
}