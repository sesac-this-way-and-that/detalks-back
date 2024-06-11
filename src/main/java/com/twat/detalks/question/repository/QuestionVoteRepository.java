package com.twat.detalks.question.repository;

import com.twat.detalks.question.entity.QuestionVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionVoteRepository extends JpaRepository<QuestionVoteEntity, Long> {
    Optional<QuestionVoteEntity> findByQuestions_QuestionIdAndMembers_MemberIdx(Long questionId, Long memberIdx);
    void deleteByQuestions_QuestionIdAndMembers_MemberIdx(Long questionId, Long memberIdx);
    int countByQuestions_QuestionIdAndVoteState(Long questionId, boolean voteState);
    List<QuestionVoteEntity> findByQuestions_QuestionId(Long questionId); // 총 투표 점수

}
