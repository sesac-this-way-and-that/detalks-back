package com.twat.detalks.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDeleteDto {

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String pwd;
    private String reason;

}
