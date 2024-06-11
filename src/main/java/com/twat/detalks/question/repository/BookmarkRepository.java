package com.twat.detalks.question.repository;

import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByMemberAndQuestion(MemberEntity member, QuestionEntity question);
    List<BookmarkEntity> findByMember(MemberEntity member);
    Boolean existsByQuestionIdAndMemberIdx(Long questionId, Long memberIdx);
    BookmarkEntity findByQuestionIdAndMemberIdx(Long questionId, Long memberIdx);
}
