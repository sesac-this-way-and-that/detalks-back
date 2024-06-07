package com.twat.detalks.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Members")
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "memberIdx")
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

    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuestionEntity> questionList;

    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnswerEntity> answerList;
}



