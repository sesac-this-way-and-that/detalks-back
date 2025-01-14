package com.twat.detalks.member.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.entity.AnswerVoteEntity;
import com.twat.detalks.member.vo.Role;
import com.twat.detalks.member.vo.Social;
import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.entity.QuestionVoteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "members")
@DynamicUpdate
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_idx", nullable = false)
    private Long memberIdx;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_isdeleted", nullable = false)
    @Builder.Default
    private Boolean memberIsDeleted = false;

    @Column(name = "member_reason")
    @Builder.Default
    private String memberReason = "";

    @Column(name = "member_state", nullable = false)
    @Builder.Default
    private Boolean memberState = true;

    @Column(name = "member_img", nullable = false)
    private String memberImg;

    @Column(name = "member_summary")
    @Builder.Default
    private String memberSummary = "";

    @Column(name = "member_about")
    @Builder.Default
    private String memberAbout = "";

    @Column(name = "member_rep", nullable = false)
    @Builder.Default
    private Integer memberRep = 1;

    @Column(name = "member_created", nullable = false)
    @Builder.Default
    private LocalDateTime memberCreated = LocalDateTime.now();

    @Column(name = "member_visited", nullable = false)
    @Builder.Default
    private LocalDateTime memberVisited = LocalDateTime.now();

    @Column(name = "member_updated")
    private LocalDateTime memberUpdated;

    @Column(name = "member_deleted")
    private LocalDateTime memberDeleted;

    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuestionEntity> questionList;

    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnswerEntity> answerList;

    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuestionVoteEntity> questionVoteList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnswerVoteEntity> answerVoteList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkEntity> bookmarkList;

    @Column(name = "member_role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role memberRole = Role.USER;

    @Column(name = "member_social", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Social memberSocial = Social.NONE;

}

