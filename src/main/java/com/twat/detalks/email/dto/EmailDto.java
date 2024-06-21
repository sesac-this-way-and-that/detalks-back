package com.twat.detalks.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EmailDto {

    // 이메일
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    @Schema(description = "이메일 주소", example = "테스트 가능한 본인 이메일 주소")
    private String email;
    // 인증 코드
    @Schema(description = "인증코드", example = "asdfasdf12")
    private String code;
}