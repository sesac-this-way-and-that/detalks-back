package com.twat.detalks.question.controller;

import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.dto.ResErrorDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    // 질문 리스트 조회
    // GET http://localhost:8080/api/questions
    @GetMapping("")
    public ResponseEntity<List<QuestionDto>> getQuestions() {
        List<QuestionDto> questions = questionService.getQuestions();
        return ResponseEntity.ok(questions);
    }

    // 특정 질문 조회
    // GET http://localhost:8080/api/questions/{questionId}
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long questionId) {
        QuestionDto questionDTO = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(questionDTO);
    }

    // 질문 생성
    // POST http://localhost:8080/api/questions
    @PostMapping("")
    public ResponseEntity<?> createQuestion(
            @AuthenticationPrincipal String memberIdx,
            @RequestBody QuestionCreateDto questionCreateDto) {
        try{
            QuestionDto newQuestion = questionService.createQuestion(memberIdx, questionCreateDto);
            return ResponseEntity.ok(newQuestion);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder()
                            .error(e.getMessage())
                            .build()
            );
        }
    }


    // 질문 수정
    // Patch http://localhost:8080/api/questions/{questionId}
    @PatchMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody QuestionCreateDto questionCreateDto,
            @AuthenticationPrincipal String memberIdx) {
        try {
            QuestionDto updatedQuestion = questionService.updateQuestion(questionId, questionCreateDto, memberIdx);
            return ResponseEntity.ok(updatedQuestion); }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }


    // 질문 삭제
    // Patch http://localhost:8080/api/questions/{questionId}
    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal String memberIdx) {

        try {
            questionService.deleteQuestion(questionId, memberIdx);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }
}