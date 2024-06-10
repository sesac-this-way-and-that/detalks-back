package com.twat.detalks.question.controller;

import com.twat.detalks.question.dto.ResErrorDto;
import com.twat.detalks.question.service.QuestionVoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/votes")
@Slf4j
public class QuestionVoteController {
    @Autowired
    private QuestionVoteService voteService;

    // 질문 투표
    // POST /api/votes/question/{questionId}
    @PostMapping("/question/{questionId}")
    public ResponseEntity<?> vote(@PathVariable Long questionId,
                                  @RequestParam Boolean voteState,
                                  @AuthenticationPrincipal String memberIdx) {
        try {
            voteService.vote(questionId, Long.parseLong(memberIdx), voteState);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }

    // 질문 투표 삭제
    // DELETE /api/votes/question/{questionId}
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<?> removeVote(
            @PathVariable Long questionId,
            @AuthenticationPrincipal String memberIdx) {
        try {
            voteService.removeVote(questionId, Long.parseLong(memberIdx));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }
}
