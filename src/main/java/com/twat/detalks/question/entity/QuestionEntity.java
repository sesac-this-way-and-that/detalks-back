package com.twat.detalks.question.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "questionId")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "question_title", nullable = false, length = 30)
    private String questionTitle;

    @Column(name = "question_content", nullable = false, length = 255)
    private String questionContent;

    @CreationTimestamp
    @Column(name = "q_created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "q_modified_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp modifiedAt;

    @Column(name = "q_view_count", nullable = false, length = 100)
    private int viewCount;

    @Column(name = "q_vote_count", nullable = false, length = 100)
    private int voteCount;

    @Column(name = "question_state", nullable = false)
    @Builder.Default
    private Boolean questionState = true;

    @Column(name = "is_solved", nullable = false)
    @Builder.Default
    private Boolean isSolved = false;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "member_idx", nullable = false)  // 차후에 탈퇴 회원 관리시 nullable을 true로 바꿀 필요있음
    private MemberEntity members;

    @OneToMany(mappedBy = "questions", cascade = CascadeType.ALL, orphanRemoval = true) // 질문 삭제 시 관련 답변 자동 삭제
    @JsonManagedReference
    private List<AnswerEntity> answerList;

}