package com.twat.detalks.answer.repository;

import com.twat.detalks.answer.entity.AnswerVoteEntity;
import com.twat.detalks.question.entity.QuestionVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerVoteRepository extends JpaRepository<AnswerVoteEntity, Long> {
    Optional<AnswerVoteEntity> findByAnswer_AnswerIdAndMember_MemberIdx(Long questionId, Long memberIdx);
    int countByAnswer_AnswerIdAndVoteState(Long answerId, Boolean voteState);

    List<AnswerVoteEntity> findByAnswer_AnswerId(Long answerId);

    void deleteByAnswer_AnswerIdAndMember_MemberIdx(Long answerId, Long memberIdx);
}
