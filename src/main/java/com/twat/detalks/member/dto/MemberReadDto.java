package com.twat.detalks.member.dto;

import com.twat.detalks.member.vo.Social;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberReadDto {

    private Long idx;
    private String email;
    private String name;
    private Boolean isDeleted;
    private String reason;
    private Boolean state;
    private String img;
    private String summary;
    private String about;
    private Integer rep;
    private Social social;
    private Long qCount;
    private Long aCount;
    private String created;
    private String visited;
    private String updated;
    private String role;
    private List<String> tags;
    private Long bookmarkCount;
}
