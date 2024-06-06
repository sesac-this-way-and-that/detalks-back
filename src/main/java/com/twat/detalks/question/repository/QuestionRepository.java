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

    // QuestionEntity findByMemberId(int member_idx);
    // QuestionEntity findByMemberIdAndQId(int member_idx, int question_id);

    // @Query("SELECT m FROM MemberEntity m JOIN FETCH m.questions q WHERE m.member_idx = :memberId")
    // QuestionEntity findQuestionsByMember(int memberId);

    // Optional<QuestionEntity> findByQuestionIdAndMember_MemberIdx(int question_id, int member_idx);

    // 조회수
    @Modifying(clearAutomatically = true)
    @Query("update QuestionEntity q set q.viewCount = q.viewCount + 1 where q = :q")
    void updateView(@Param("q") QuestionEntity questions);
}
