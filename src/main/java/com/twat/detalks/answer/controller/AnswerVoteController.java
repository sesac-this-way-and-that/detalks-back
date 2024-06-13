package com.twat.detalks.answer.controller;

import com.twat.detalks.answer.service.AnswerVoteService;
import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
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
            @AuthenticationPrincipal CustomUserDetail user) {
        String memberIdx = user.getUserIdx();
        try {
            answerVoteService.voteAnswer(answerId, Long.parseLong(memberIdx), voteState);
            String msg = "";
            if (voteState) {
                msg = "답변에 찬성했습니다.";
            }
            else msg = "답변에 반대 했습니다.";
            ResDto response = ResDto.builder()
                    .result(true)
                    .msg(msg)
                    .status("200")
                    .token(memberIdx)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResDto.builder()
                            .msg("답변 투표 실패")
                            .status("400")
                            .errorType(e.getMessage())
                            .result(false)
                            .build());
        }
    }

    // 답변 투표 취소
    // DELETE /api/votes/answer/{answerId}
    @DeleteMapping("/answer/{answerId}")
    public ResponseEntity<?> removeVote(
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetail user) {
        String memberIdx = user.getUserIdx();
        try {
            answerVoteService.removeVote(answerId, Long.parseLong(memberIdx));
            return ResponseEntity.ok().body(
                    ResDto.builder()
                            .msg("답변 투표 취소 성공")
                            .result(true)
                            .status("200")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResDto.builder()
                            .msg("답변 투표 취소 실패")
                            .status("400")
                            .errorType(e.getMessage())
                            .result(false)
                            .build());
        }
    }
}
