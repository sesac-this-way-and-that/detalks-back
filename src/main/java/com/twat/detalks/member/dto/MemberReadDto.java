package com.twat.detalks.member.dto;

import com.twat.detalks.member.vo.Social;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MemberReadDto {
    private Long memberIdx;
    private String memberEmail;
    private String memberName;
    private Boolean memberIsDeleted;
    private String memberReason;
    private Boolean memberState;
    private String memberImg;
    private String memberSummary;
    private String memberAbout;
    private Integer memberRep;
    private Social memberSocial;
    private Integer memberQcount;
    private Integer memberAcount;
    private String memberCreated;
    private String memberVisited;
    private String memberUpdated;
}
