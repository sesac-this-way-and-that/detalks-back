package com.twat.detalks.question.repository;

import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByMember_MemberIdxAndQuestion_QuestionId(Long memberIdx, Long questionId);
    // List<BookmarkEntity> findByMember_MemberIdx(Long memberIdx);
    Page<BookmarkEntity> findByMember_MemberIdx(Long memberIdx, Pageable pageable);
    boolean existsByMember_MemberIdxAndQuestion_QuestionId(Long memberIdx, Long questionId);

    List<BookmarkEntity> findByMember_MemberIdxAndBookmarkState(Long memberIdx, Boolean bookmarkState);

    Page<BookmarkEntity> findByMember_MemberIdxOrderByQuestion_VoteCountDesc(Long memberIdx, Pageable pageable);
}
