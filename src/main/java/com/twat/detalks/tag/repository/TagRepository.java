package com.twat.detalks.tag.repository;

import com.twat.detalks.tag.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTagName(String tagName);
}
