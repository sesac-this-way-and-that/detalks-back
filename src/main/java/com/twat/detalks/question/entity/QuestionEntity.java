package com.twat.detalks.question.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.tag.entity.QuestionTagEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "question_title", nullable = false)
    private String questionTitle;

    @Lob //  데이터베이스의 BLOB, CLOB 타입과 매핑되어 파일이나 이미지 같은 큰 데이터를 저장하는 데 사용
    @Column(name = "question_content", columnDefinition = "TEXT", nullable = false) //특정 데이터베이스에서만 지원하는 텍스트 타입을 지정하고 싶을 때 사용
    private String questionContent;

    @CreatedDate
    @Column(name = "q_created_at", nullable = false) // columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "q_modified_at", nullable = false) //  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    private LocalDateTime modifiedAt;

    @Column(name = "q_view_count", nullable = false)
    private int viewCount;

    @Column(name = "q_vote_count", nullable = false)
    private int voteCount;

    @Column(name = "question_state", nullable = false)
    @Builder.Default
    private Boolean questionState = true;

    @Column(name = "is_solved", nullable = false)
    @Builder.Default
    private Boolean isSolved = false;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "member_idx", nullable = true)  // 차후에 탈퇴 회원 관리시 nullable을 true로 바꿀 필요있음
    private MemberEntity members;

    @OneToMany(mappedBy = "questions", cascade = CascadeType.ALL, orphanRemoval = true) // 질문 삭제 시 관련 답변 자동 삭제
    @JsonManagedReference
    private List<AnswerEntity> answerList;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<QuestionTagEntity> questionTagList;

    @OneToMany(mappedBy = "questions", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuestionVoteEntity> questionVoteList;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkEntity> bookmarks;
    // private Set<BookmarkEntity> bookmarks;

    @Column(name = "question_rep", nullable = false)
    @Builder.Default
    private Integer questionRep = 0;
}
