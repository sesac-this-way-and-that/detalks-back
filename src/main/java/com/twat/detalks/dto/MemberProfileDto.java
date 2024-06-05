package com.twat.detalks.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileDto {
    private Long memberIdx;
    private String memberEmail;
    private String memberName;
    private boolean memberIsDeleted;
    private String memberReason;
}
