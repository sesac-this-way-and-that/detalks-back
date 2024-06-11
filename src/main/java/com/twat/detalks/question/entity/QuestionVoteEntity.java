package com.twat.detalks.question.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_votes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_vote_id", nullable = false)
    private Long voteId;

    @Column(name = "q_vote_state", nullable = true)
    private Boolean voteState;

    public Boolean isVoteState() {
        return voteState;
    }

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference
    private QuestionEntity questions;

    @ManyToOne
    @JoinColumn(name = "member_idx", nullable = false)
    @JsonBackReference
    private MemberEntity members;
}
