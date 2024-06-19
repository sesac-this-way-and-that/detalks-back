package com.twat.detalks.question.controller;

import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.question.dto.ResErrorDto;
import com.twat.detalks.question.service.QuestionVoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/votes")
@Tag(name = "투표 API (질문)", description = "질문 UP/DOWN 투표 API")
@Slf4j
public class QuestionVoteController {
    @Autowired
    private QuestionVoteService voteService;

    // 질문 투표
    // POST /api/votes/question/{questionId}
    @PostMapping("/question/{questionId}")
    @Operation(summary = "질문 투표")
    public ResponseEntity<?> vote(
        @Parameter(name = "questionId", description = "질문 아이디", example = "1")
        @PathVariable Long questionId,
        @Parameter(name = "voteState", description = "투표상태 true/false", example = "true")
        @RequestParam Boolean voteState,
        @AuthenticationPrincipal CustomUserDetail user) {
        String memberIdx = user.getUserIdx();
        try {
            voteService.vote(questionId, Long.parseLong(memberIdx), voteState);
            String msg = "";
            if (voteState) {
                msg = "질문에 찬성했습니다.";
            } else msg = "질문에 반대 했습니다.";

            ResDto response = ResDto.builder()
                .result(true)
                .msg(msg)
                .status("200")
                .token(memberIdx)
                .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResDto response = ResDto.builder()
                .result(false)
                .msg("질문 투표 실패")
                .data(null)
                .status("400")
                .errorType(e.getMessage())
                .token(memberIdx)
                .build();

            return ResponseEntity.status(400).body(response);
        }
    }

    // 질문 투표 삭제
    // DELETE /api/votes/question/{questionId}
    @DeleteMapping("/question/{questionId}")
    @Operation(summary = "질문 투표 삭제")
    public ResponseEntity<?> removeVote(
        @Parameter(name = "questionId", description = "질문 아이디", example = "1")
        @PathVariable Long questionId,
        @AuthenticationPrincipal CustomUserDetail user
    ) {
        String memberIdx = user.getUserIdx();
        try {
            voteService.removeVote(questionId, Long.parseLong(memberIdx));
            // String msg = "질문에 대한 투표가 취소되었습니다.";
            // return ResponseEntity.ok(msg);
            // return ResponseEntity.ok().build();
            ResDto response = ResDto.builder()
                .result(true)
                .msg("질문 투표 취소 성공")
                .status("200")
                .token(memberIdx)
                .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                ResDto.builder()
                    .msg("질문 투표 취소 실패")
                    .status("400")
                    .errorType(e.getMessage())
                    .token(memberIdx)
                    .result(false)
                    .build());
        }
    }
}
