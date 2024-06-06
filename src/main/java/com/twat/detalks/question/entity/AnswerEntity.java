package com.twat.detalks.question.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private long answerId;

    @Column(name = "answer_content", nullable = false)
    private String answerContent;

    @CreationTimestamp
    @Column(name = "a_created_at", nullable = false)
    private String createdAt;

    @UpdateTimestamp
    @Column(name = "a_modified_at", nullable = false)
    private String modifiedAt;

    @Column(name = "answer_state", nullable = false)
    @Builder.Default
    private Boolean answerState = true;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    @JsonBackReference
    private QuestionEntity questions;
}
