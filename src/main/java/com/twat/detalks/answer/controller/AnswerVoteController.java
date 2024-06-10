package com.twat.detalks.answer.controller;

import com.twat.detalks.answer.service.AnswerVoteService;
import com.twat.detalks.question.dto.ResErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class AnswerVoteController {
    @Autowired
    private AnswerVoteService answerVoteService;

    // 답변 투표
    // POST /api/votes/answer/{answerId}
    @PostMapping("/answer/{answerId}")
    public ResponseEntity<?> voteAnswer(
            @PathVariable Long answerId,
            @RequestParam Boolean voteState,
            @AuthenticationPrincipal String memberIdx) {
        try {
            answerVoteService.voteAnswer(answerId, Long.parseLong(memberIdx), voteState);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }

    // 답변 투표 취소
    // DELETE /api/votes/answer/{answerId}
    @DeleteMapping("/answer/{answerId}")
    public ResponseEntity<?> removeVote(
            @PathVariable Long answerId,
            @AuthenticationPrincipal String memberIdx) {
        try {
            answerVoteService.removeVote(answerId, Long.parseLong(memberIdx));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }
}
