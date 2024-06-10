package com.twat.detalks.tag.entity;

import com.twat.detalks.question.entity.QuestionEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_tags")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_tag_id", nullable = false)
    private Long questionTagId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tags;
}
