package com.twat.detalks.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberQuestionDto {
    private Long memberIdx;
    private String memberName;
    private String memberImg;
    private Integer memberRep;
}
