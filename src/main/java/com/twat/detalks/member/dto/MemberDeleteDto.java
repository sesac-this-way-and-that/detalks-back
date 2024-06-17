package com.twat.detalks.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDeleteDto {

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Schema(description = "비밀번호",example = "qwer123!@#")
    private String pwd;

    @Schema(description = "탈퇴 사유",example = "더 이상 서비스를 이용하지 않습니다.")
    private String reason;

}
