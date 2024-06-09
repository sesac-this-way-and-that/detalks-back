package com.twat.detalks.member.dto;

import com.twat.detalks.member.validation.NotEmptyMultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberUpdateDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String name;

    // @NotEmptyMultipartFile(message = "이미지 파일은 필수 입력 값입니다.")
    // private MultipartFile img;

    private String summary;

    private String about;

    private LocalDateTime updated;

    @Override
    public String toString() {
        return "MemberUpdateDto{" +
            "name='" + name + '\'' +
            ", summary='" + summary + '\'' +
            ", about='" + about + '\'' +
            ", updated=" + updated +
            '}';
    }
}
