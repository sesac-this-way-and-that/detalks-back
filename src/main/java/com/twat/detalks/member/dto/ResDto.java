package com.twat.detalks.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ResDto {
    // 응답객체 통합 DTO (에러 포함)
    private boolean result;
    private String msg;
    private Object data;
    private String errorType; // 에러 타입
    private String token;
}
