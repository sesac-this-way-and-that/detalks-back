package com.twat.detalks.answer.repository;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepositroy extends JpaRepository<AnswerEntity, Long> {
    Optional<AnswerEntity> findByQuestionsAndMembers(QuestionEntity question, MemberEntity member);
    List<AnswerEntity> findByMembers_MemberIdx(Long memberIdx);
    List<AnswerEntity> findByQuestions_QuestionId(Long questionId);

    Page<AnswerEntity> findByMembers_MemberIdxAndIsSelectedFalse(Long memberIdx, Pageable pageable);
    long countAllByMembersEquals(MemberEntity members);

}
