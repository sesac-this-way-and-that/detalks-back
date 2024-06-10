package com.twat.detalks.tag.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = "tag_name")) // 태그명 중복되지 않기 위해 유나크 제약 조건
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @OneToMany(mappedBy = "tags")
    private Set<QuestionTagEntity> questionTagList;

    // 태그명 설정
    public TagEntity(String tagName) {
        this.tagName = tagName;
    }
}
