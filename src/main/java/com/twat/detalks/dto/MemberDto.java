package com.twat.detalks.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    private String memberEmail;
    private String memberPwd;
    private String memberName;
}
