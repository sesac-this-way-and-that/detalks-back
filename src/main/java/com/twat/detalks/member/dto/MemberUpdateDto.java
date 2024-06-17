package com.twat.detalks.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberUpdateDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    @Schema(description = "수정할 이름", example = "테스트")
    private String name;

    @Schema(description = "한줄 소개", example = "한줄 소개입니다.")
    private String summary;

    @Schema(description = "자기 소개", example = "자기 소개입니다.")
    private String about;

    @Schema(description = "기본값 입력 필요 X")
    private LocalDateTime updated;

}
