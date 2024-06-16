package com.twat.detalks.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberForm {
    @Schema(description = "이메일", example = "test@test.com")
    private String email;
    @Schema(description = "비밀번호", example = "qwer123!@#")
    private String pwd;
}
