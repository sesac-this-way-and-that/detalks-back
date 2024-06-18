package com.twat.detalks.answer.controller;

import com.twat.detalks.answer.dto.AnswerCreateDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.service.AnswerService;
import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.question.dto.ResErrorDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/questions")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    // 답변 생성
    // POST /api/questions/{questionId}/answers
    @PostMapping("/{questionId}/answers")
    public ResponseEntity<?> createAnswer(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long questionId,
            @RequestBody AnswerCreateDto answerCreateDto) {
        String memberIdx = user.getUserIdx();
        try {
            AnswerEntity newAnswer = answerService.createAnswer(memberIdx, questionId, answerCreateDto);
            ResDto response = ResDto.builder()
                    .result(true)
                    .msg("답변 생성 성공")
                    .data(newAnswer)
                    .status("200")
                    .token(String.valueOf(memberIdx))
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResDto.builder()
                            .msg("답변 생성 실패")
                            .status("400")
                            .errorType(e.getMessage())
                            .result(false)
                            .build());
        }
    }

    // 답변 수정
    // PATCH /api/questions/answers/{answerId}
    @PatchMapping("/answers/{answerId}")
    public ResponseEntity<?> updateAnswer(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long answerId,
            @Valid @RequestBody AnswerCreateDto answerCreateDto) {
        String memberIdx = user.getUserIdx();
        try {
            AnswerEntity updatedAnswer = answerService.updateAnswer(memberIdx, answerId, answerCreateDto);
            ResDto response = ResDto.builder()
                    .result(true)
                    .msg("답변 수정 성공")
                    .data(updatedAnswer)
                    .status("200")
                    .token(String.valueOf(memberIdx))
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResDto.builder()
                            .msg("답변 수정 실패")
                            .status("400")
                            .errorType(e.getMessage())
                            .result(false)
                            .build());
        }
    }

    // 답변 삭제
    // DELETE /api/questions/answers/{answerId}
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long answerId) {
        String memberIdx = user.getUserIdx();
        try {
            answerService.deleteAnswer(memberIdx, answerId);
            return ResponseEntity.ok().body(
                    ResDto.builder()
                            .msg("답변 삭제 성공")
                            .result(true)
                            .status("200")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResDto.builder()
                            .msg("답변 삭제 실패")
                            .status("400")
                            .errorType(e.getMessage())
                            .result(false)
                            .build());
        }
    }

    // 답변 채택
    // PATCH /api/questions/{questionId}/{answerId}/select
    @PatchMapping("/{questionId}/{answerId}/select")
    public ResponseEntity<?> selectAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetail user) {
        String memberIdx = user.getUserIdx();
        try {
            answerService.selectAnswer(questionId, answerId, Long.parseLong(memberIdx));
            return ResponseEntity.ok().body(
                    ResDto.builder()
                            .msg("답변 채택 성공")
                            .result(true)
                            .status("200")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResDto.builder()
                            .msg("답변 채택 실패")
                            .status("400")
                            .errorType(e.getMessage())
                            .result(false)
                            .build());
        }
    }
}
