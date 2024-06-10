package com.twat.detalks.question.repository;

import com.twat.detalks.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    Optional<QuestionEntity> findById(Long questionId);
    Optional<List<QuestionEntity>> findByMembersMemberIdx(Long memberIdx);


    // 조회수 업데이트
    @Modifying
    @Query("UPDATE QuestionEntity q SET q.viewCount = q.viewCount + 1 WHERE q.id = :questionId")
    void updateViewCount(@Param("questionId") Long questionId);

    // int countByQuestionIdAndVoteState(Long questionId, Boolean voteState);
}
