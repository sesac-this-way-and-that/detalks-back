package com.twat.detalks.question.controller;

import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.question.dto.BookmarkedQuestionDto;
import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.question.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    // 북마크 추가
    // POST /api/bookmarks/add
    @PostMapping("/add")
    public ResponseEntity<BookmarkEntity> addBookmark(@AuthenticationPrincipal CustomUserDetail user, @RequestParam Long questionId) {
        String memberIdx = user.getUserIdx();
        bookmarkService.addBookmark(Long.valueOf(memberIdx), questionId);
        return ResponseEntity.ok().build();
    }

    // 북마크 삭제
    // DELETE /api/bookmarks/remove
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(@AuthenticationPrincipal CustomUserDetail user, @RequestParam Long questionId) {
        String memberIdx = user.getUserIdx();
        bookmarkService.removeBookmark(Long.valueOf(memberIdx), questionId);
        return ResponseEntity.noContent().build();
    }

    // 북마크 리스트 조회
    // GET /api/bookmarks
    @GetMapping
    public ResponseEntity<List<BookmarkedQuestionDto>> getBookmarks(@AuthenticationPrincipal CustomUserDetail user) {
        String memberIdx = user.getUserIdx();
        List<BookmarkedQuestionDto> bookmarks = bookmarkService.getBookmarksByMember(Long.valueOf(memberIdx));
        return ResponseEntity.ok(bookmarks);
    }
}
