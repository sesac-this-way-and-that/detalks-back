package com.twat.detalks.question.controller;

import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.question.dto.BookmarkedQuestionDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.question.service.BookmarkService;
import com.twat.detalks.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookmarks")
@Tag(name = "북마크 API", description = "회원이 북마크한 질문 관련 API")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private QuestionService questionService;

    // 북마크 추가
    // POST /api/bookmarks/{questionId}
    @PostMapping("/{questionId}")
    @Operation(summary = "북마크 추가")
    public ResponseEntity<ResDto> addBookmark(
        @Parameter(name = "questionId", description = "질문 아이디")
        @PathVariable Long questionId,
        @AuthenticationPrincipal CustomUserDetail user) {
        if (user != null) {
            Long memberIdx = Long.valueOf(user.getUserIdx());
            bookmarkService.addBookmark(memberIdx, questionId);
            ResDto response = ResDto.builder()
                .result(true)
                .msg("북마크 추가 성공")
                .status("200")
                .token(String.valueOf(memberIdx))
                .build();

            return ResponseEntity.ok(response);
        } else {
            ResDto response = ResDto.builder()
                .result(false)
                .msg("북마크 추가 실패")
                .status("400")
                .token(null)
                .build();

            return ResponseEntity.status(400).body(response);
        }
    }

    // 북마크 삭제
    // DELETE /api/bookmarks/{questionId}
    @DeleteMapping("/{questionId}")
    @Operation(summary = "북마크 삭제")
    public ResponseEntity<ResDto> removeBookmark(
        @Parameter(name = "questionId", description = "질문 아이디")
        @PathVariable Long questionId,
        @AuthenticationPrincipal CustomUserDetail user) {
        if (user != null) {
            Long memberIdx = Long.valueOf(user.getUserIdx());
            bookmarkService.removeBookmark(memberIdx, questionId);
            return ResponseEntity.ok().body(
                ResDto.builder()
                    .msg("북마크 삭제 성공")
                    .result(true)
                    .status("200")
                    .token(String.valueOf(memberIdx))
                    .build());
        } else {
            ResDto response = ResDto.builder()
                .result(false)
                .msg("북마크 삭제 실패")
                .status("400")
                .token(null)
                .build();

            return ResponseEntity.status(400).body(response);
        }
    }

    // 북마크 리스트 조회
    // GET /api/bookmarks
    @GetMapping
    @Operation(summary = "북마크 리스트 조회")
    public ResponseEntity<?> getBookmarks(
        @AuthenticationPrincipal CustomUserDetail user,
        @Parameter(name = "page",description = "페이지 기본값 [0]")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(name = "size",description = "사이즈 기본값 [10]")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(name = "bookmarkId",description = "북마크 아이디")
        @RequestParam(defaultValue = "bookmarkId") String sortBy
    ) {
        if (user != null) {
            Long memberIdx = Long.valueOf(user.getUserIdx());
            Page<BookmarkedQuestionDto> bookmarkedQuestionsPage = bookmarkService.getBookmarksByMember(memberIdx, page, size, sortBy);

            return ResponseEntity.ok().body(
                ResDto.builder()
                    .msg("북마크 리스트 조회 성공")
                    .data(bookmarkedQuestionsPage)
                    .result(true)
                    .status("200")
                    .token(String.valueOf(memberIdx))
                    .build());
        } else {
            ResDto response = ResDto.builder()
                .result(false)
                .msg("북마크 리스트 조회 실패")
                .status("400")
                .token(null)
                .build();

            return ResponseEntity.status(400).body(response);
        }
    }
}
