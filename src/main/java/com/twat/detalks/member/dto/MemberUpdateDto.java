package com.twat.detalks.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberUpdateDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String memberName;

    @NotBlank(message = "이미지 경로는 필수 입력 값입니다.")
    private String memberImg;

    private String memberSummary;

    private String memberAbout;

    private LocalDateTime memberUpdated;
}
