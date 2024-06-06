package com.twat.detalks.answer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.twat.detalks.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@Table(name = "answers")
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "answer_content", nullable = false)
    private String answerContent;

    @CreationTimestamp
    @Column(name = "a_created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "a_modified_at", nullable = false)
    private Timestamp modifiedAt;

    @Column(name = "answer_state", nullable = false)
    @Builder.Default
    private Boolean answerState = true;

    @Column(name = "a_vote_count", nullable = false, length = 100)
    private int voteCount;

    @Column(name = "is_selected", nullable = false)
    @Builder.Default
    private Boolean isSelected = false;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    @JsonBackReference
    private QuestionEntity questions;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "memberIdx", nullable = false)  // 차후에 탈퇴 회원 관리시 nullable을 true로 바꿀 필요있음
    private MemberEntity members;
}