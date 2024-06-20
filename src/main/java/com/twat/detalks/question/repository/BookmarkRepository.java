package com.twat.detalks.question.repository;

import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByMember_MemberIdxAndQuestion_QuestionId(Long memberIdx, Long questionId);
    // List<BookmarkEntity> findByMember_MemberIdx(Long memberIdx);
    Page<BookmarkEntity> findByMember_MemberIdx(Long memberIdx, Pageable pageable);

    @Query("SELECT b.question.questionId FROM BookmarkEntity b WHERE b.member.memberIdx = :memberId")
    List<Long> findQuestionIdsByMemberId(@Param("memberId") Long memberId);
    // 회원 아이디로 북마크 리스트 카운트
    Long countAllByMember_MemberIdx(Long member_memberIdx);
}
