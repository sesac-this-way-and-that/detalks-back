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
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public void addBookmark(Long memberId, Long questionId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        QuestionEntity question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));

        BookmarkEntity bookmark = BookmarkEntity.builder()
                .member(member)
                .question(question)
                .build();

        bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Long memberId, Long questionId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        QuestionEntity question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));

        BookmarkEntity bookmark = bookmarkRepository.findByMemberAndQuestion(member, question)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmarkRepository.delete(bookmark);
    }

    public List<BookmarkedQuestionDto> getBookmarksByMember(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        List<BookmarkEntity> bookmarks = bookmarkRepository.findByMember(member);

        return bookmarks.stream()
                .map(bookmark -> BookmarkedQuestionDto.builder()
                        .bookmarkId(bookmark.getBookmarkId())
                        .question(toQuestionDto(bookmark.getQuestion()))
                        .build())
                .collect(Collectors.toList());
    }

    private QuestionDto toQuestionDto(QuestionEntity questionEntity) {
        return QuestionDto.builder()
                .questionId(questionEntity.getQuestionId())
                .questionTitle(questionEntity.getQuestionTitle())
                .questionContent(questionEntity.getQuestionContent())
                .createdAt(questionEntity.getCreatedAt())
                .modifiedAt(questionEntity.getModifiedAt())
                .viewCount(questionEntity.getViewCount())
                .voteCount(questionEntity.getVoteCount())
                .questionState(questionEntity.getQuestionState())
                .isSolved(questionEntity.getIsSolved())
                .author(MemberQuestionDto.builder()
                        .memberIdx(questionEntity.getMembers().getMemberIdx())
                        .memberName(questionEntity.getMembers().getMemberName())
                        .build())
                .answerList(questionEntity.getAnswerList().stream()
                        .map(answer -> AnswerDto.builder()
                                .answerId(answer.getAnswerId())
                                .answerContent(answer.getAnswerContent())
                                .createdAt(answer.getCreatedAt())
                                .modifiedAt(answer.getModifiedAt())
                                .isSelected(answer.getIsSelected())
                                .build())
                        .collect(Collectors.toList()))
                .tagNameList(questionEntity.getQuestionTagList().stream()
                        .map(tag -> tag.getTags().getTagName())
                        .collect(Collectors.toList()))
                .build();
    }
}
