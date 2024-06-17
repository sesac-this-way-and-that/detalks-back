package com.twat.detalks.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangePwdDto {

    @NotBlank
    @Schema(description = "비밀번호", example = "qwer123!@#")
    private String pwd;

    @NotBlank
    @Schema(description = "변경된 비밀번호", example = "qwer123!")
    private String changePwd;
}
