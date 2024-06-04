package com.twat.detalks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResDto {
    private boolean result;
    private String msg;
    private Object data;
}
