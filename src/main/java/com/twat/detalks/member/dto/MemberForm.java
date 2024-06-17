package com.twat.detalks.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberForm {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Schema(description = "이메일 주소",example = "test@test.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Schema(description = "비밀번호", example = "qwer123!@#")
    private String pwd;
}