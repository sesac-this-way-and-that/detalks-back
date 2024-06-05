package com.twat.detalks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Members")
public class MemberEntity {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="member_idx", nullable = false)
    private Long memberIdx;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_isdeleted", nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private boolean memberIsDeleted = false;

    @Column(name = "member_reason", nullable = true)
    private String memberReason;
}



