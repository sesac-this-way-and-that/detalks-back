package com.twat.detalks.question.service;

import com.twat.detalks.answer.dto.AnswerDto;
import com.twat.detalks.question.dto.BookmarkedQuestionDto;
import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.question.repository.BookmarkRepository;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.dto.MemberQuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // 북마크 추가
    public BookmarkEntity addBookmark(Long memberIdx, Long questionId) {
        MemberEntity member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        Optional<BookmarkEntity> bookmarkOpt = bookmarkRepository.findByMember_MemberIdxAndQuestion_QuestionId(memberIdx, questionId);
        BookmarkEntity bookmark;

        if (bookmarkOpt.isPresent()) {
            bookmark = bookmarkOpt.get();
            bookmark.setBookmarkState(true);
        } else {
            bookmark = BookmarkEntity.builder()
                    .member(member)
                    .question(question)
                    .bookmarkState(true)
                    .build();
        }

        return bookmarkRepository.save(bookmark);
    }

    // 북마크 삭제
    public void removeBookmark(Long memberIdx, Long questionId) {
        Optional<BookmarkEntity> bookmarkOpt = bookmarkRepository.findByMember_MemberIdxAndQuestion_QuestionId(memberIdx, questionId);
        if (bookmarkOpt.isPresent()) {
            BookmarkEntity bookmark = bookmarkOpt.get();
            bookmark.setBookmarkState(false);
            bookmarkRepository.save(bookmark);
        } else {
            throw new RuntimeException("북마크가 존재하지 않습니다.");
        }
    }

    // 북마크 리스트 조회
    public List<BookmarkEntity> getBookmarksByMember(Long memberIdx) {
        return bookmarkRepository.findByMember_MemberIdxAndBookmarkState(memberIdx, true);
    }

    // 상세 질문 조회 시 북마크 상태 업데이트
    public Boolean updateBookmarkState(Long memberIdx, Long questionId) {
        Boolean bookmarkState = false;
        if (memberIdx != null) {
            BookmarkEntity bookmark = bookmarkRepository.findByMember_MemberIdxAndQuestion_QuestionId(memberIdx, questionId)
                    .orElse(null);
            if (bookmark != null) {
                bookmarkState = bookmark.getBookmarkState();
            }
        }
        return bookmarkState;
    }
}