package com.twat.detalks.member.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Social {
    NONE("NONE"),
    GOOGLE("GOOGLE"),
    GITHUB("GITHUB");
    private final String key;
}
