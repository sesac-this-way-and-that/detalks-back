package com.twat.detalks.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ResDto {

    private boolean result;
    private String msg;
    private Object data;
    private String status;
    private String errorType;
    private String token;

}
