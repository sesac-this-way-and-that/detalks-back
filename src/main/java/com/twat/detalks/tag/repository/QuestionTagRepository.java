package com.twat.detalks.tag.repository;

import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.tag.entity.QuestionTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTagRepository extends JpaRepository<QuestionTagEntity, Long> {
    List<QuestionTagEntity> findByQuestion(QuestionEntity question);
    void deleteByQuestion(QuestionEntity questions);
}
