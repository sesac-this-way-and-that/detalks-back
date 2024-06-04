package com.twat.detalks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name="Members")
@NoArgsConstructor
@AllArgsConstructor
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
}



