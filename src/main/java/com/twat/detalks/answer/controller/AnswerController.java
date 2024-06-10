package com.twat.detalks.answer.controller;

import com.twat.detalks.answer.dto.AnswerCreateDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.service.AnswerService;
import com.twat.detalks.question.dto.ResErrorDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            @AuthenticationPrincipal String memberId,
            @PathVariable Long questionId,
             @RequestBody AnswerCreateDto answerCreateDto) {
        try {
            AnswerEntity newAnswer = answerService.createAnswer(memberId, questionId, answerCreateDto);
            return ResponseEntity.ok(newAnswer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }

    // 답변 수정
    // PATCH /api/questions/answers/{answerId}
    @PatchMapping("/answers/{answerId}")
    public ResponseEntity<?> updateAnswer(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long answerId,
            @Valid @RequestBody AnswerCreateDto answerCreateDto) {
        try {
            AnswerEntity updatedAnswer = answerService.updateAnswer(memberId, answerId, answerCreateDto);
            return ResponseEntity.ok(updatedAnswer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }

    // 답변 삭제
    // DELETE /api/questions/answers/{answerId}
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long answerId) {
        try {
            answerService.deleteAnswer(memberId, answerId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }
}
