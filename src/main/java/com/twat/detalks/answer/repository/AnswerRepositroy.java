package com.twat.detalks.answer.repository;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepositroy extends JpaRepository<AnswerEntity, Long> {
}
