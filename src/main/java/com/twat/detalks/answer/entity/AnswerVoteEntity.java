package com.twat.detalks.answer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.twat.detalks.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer_votes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerVoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_vote_id", nullable = false)
    private Long voteId;

    @Column(name = "a_vote_state", nullable = true)
    @Builder.Default
    private Boolean voteState = null;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    @JsonBackReference
    private AnswerEntity answer;

    @ManyToOne
    @JoinColumn(name = "member_idx", nullable = false)
    @JsonBackReference
    private MemberEntity member;
}
