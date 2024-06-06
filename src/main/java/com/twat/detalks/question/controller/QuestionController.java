package com.twat.detalks.question.controller;

import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.dto.ResErrorDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    // 질문 리스트 조회
    @GetMapping("")
    public ResponseEntity<List<QuestionDto>> getQuestions() {
        List<QuestionDto> questions = questionService.getQuestions();
        return ResponseEntity.ok(questions);
    }

    // 특정 질문 조회
    @GetMapping("/{question_id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long question_id) {
        QuestionDto questionDTO = questionService.getQuestionById(question_id);
        return ResponseEntity.ok(questionDTO);
    }

    // 추가
    @PostMapping("")
    public ResponseEntity<?> createQuestion(
            @AuthenticationPrincipal String id,
            @Valid @RequestBody QuestionCreateDto question) {
        try{
            QuestionEntity newQuestion = questionService.createQuestion(id, question);
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

    // 수정
    @PatchMapping("/{question_id}")
    public ResponseEntity<?> updateQuestion(
            @AuthenticationPrincipal String id,
            @PathVariable Long question_id,
            @RequestBody QuestionCreateDto question
    ) {
        try {
            // Long tempMem = 2L;
            return ResponseEntity.ok(questionService.updateQuestion(id, question_id, question));
            // return ResponseEntity.ok(questionService.updateQuestion(tempMem, question_id, question));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }

    @DeleteMapping("/{question_id}")
    public ResponseEntity<?> deleteQuestion(
            @PathVariable Long question_id
    ) {
        try {
            return ResponseEntity.ok(questionService.deleteQuestion(question_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResErrorDto.builder().error(e.getMessage()).build()
            );
        }
    }
}
