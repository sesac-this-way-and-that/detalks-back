package com.twat.detalks.question.controller;

import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.dto.ResErrorDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.service.QuestionSearchService;
import com.twat.detalks.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@Slf4j
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionSearchService questionSearchService;

    // 질문 리스트 조회
    // GET /api/questions
    @GetMapping("")
    public ResponseEntity<?> getQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);


        Page<QuestionDto> questionsPage = questionService.getQuestions(pageable);
        if (questionsPage.isEmpty()) {
            ResDto response = ResDto.builder()
                    .result(false)
                    .msg("질문이 없습니다.")
                    .data(null)
                    .status("404")
                    .errorType("No Results Found")
                    .token(null)
                    .build();

            return ResponseEntity.status(404).body(response);
        }

        ResDto response = ResDto.builder()
                .result(true)
                .msg("질문 리스트 조회 성공")
                .data(questionsPage)
                .status("200")
                .token(null)
                .build();

        return ResponseEntity.ok(response);
    }

    // answerList가 null인 질문 리스트 조회
    // GET /api/questions/unanswered
    @GetMapping("/api/questions/unAnswered")
    public ResponseEntity<?> getUnansweredQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<QuestionDto> questionsPage = questionService.getQuestionsWithoutAnswers(pageable);
        if (questionsPage.isEmpty()) {
            ResDto response = ResDto.builder()
                    .result(false)
                    .msg("답변이 없는 질문이 없습니다.")
                    .data(null)
                    .status("404")
                    .errorType("No Results Found")
                    .token(null)
                    .build();

            return ResponseEntity.status(404).body(response);
        }

        ResDto response = ResDto.builder()
                .result(true)
                .msg("답변이 없는 질문 리스트 조회 성공")
                .data(questionsPage)
                .status("200")
                .token(null)
                .build();

        return ResponseEntity.ok(response);
    }

    // 특정 질문 조회
    // GET /api/questions/{questionId}
    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionById(
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        // 인증된 사용자가 없으면 user는 null
        Long memberIdx = null;
        if (user != null) {
            memberIdx = Long.valueOf(user.getUserIdx());
        }

        try{
            QuestionDto questionDTO = questionService.getQuestionById(questionId, memberIdx);
            ResDto response = ResDto.builder()
                    .result(true)
                    .msg("질문 조회 성공")
                    .data(questionDTO)
                    .status("200")
                    .token(String.valueOf(memberIdx))
                    .build();

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            ResDto response = ResDto.builder()
                    .result(false)
                    .msg("질문 조회 실패")
                    .data(null)
                    .status("400")
                    .errorType(e.getMessage())
                    .token(String.valueOf(memberIdx))
                    .build();

            return ResponseEntity.status(400).body(response);
        }
    }

    // 질문 생성
    // POST /api/questions
    @PostMapping("")
    public ResponseEntity<?> createQuestion(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody QuestionCreateDto questionCreateDto) {
        String memberIdx = user.getUserIdx();
        try{
            QuestionDto newQuestion = questionService.createQuestion(memberIdx, questionCreateDto);
            ResDto response = ResDto.builder()
                    .result(true)
                    .msg("질문 생성 성공")
                    .data(newQuestion)
                    .status("200")
                    .token(memberIdx) // 넣어주는 이유??
                    .build();

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            ResDto response = ResDto.builder()
                    .result(false)
                    .msg("질문 생성 실패")
                    .data(null)
                    .status("400")
                    .errorType(e.getMessage())
                    .token(memberIdx)
                    .build();

            return ResponseEntity.status(400).body(response);
        }
    }


    // 질문 수정
    // PATCH /api/questions/{questionId}
    @PatchMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody QuestionCreateDto questionCreateDto,
            @AuthenticationPrincipal CustomUserDetail user) {
        String memberIdx = user.getUserIdx();
        try {
            QuestionDto updatedQuestion = questionService.updateQuestion(questionId, questionCreateDto, Long.valueOf(memberIdx));
            ResDto response = ResDto.builder()
                    .result(true)
                    .msg("질문 수정 성공")
                    .data(updatedQuestion)
                    .status("200")
                    .token(memberIdx)
                    .build();

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            ResDto response = ResDto.builder()
                    .result(false)
                    .msg("질문 수정 실패")
                    .data(null)
                    .status("400")
                    .errorType(e.getMessage())
                    .token(memberIdx)
                    .build();

            return ResponseEntity.status(400).body(response);
        }
    }


    // 질문 삭제
    // DELETE /api/questions/{questionId}
    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        String memberIdx = user.getUserIdx();
        try {
            questionService.deleteQuestion(questionId, Long.valueOf(memberIdx));
            ResDto response = ResDto.builder()
                    .result(true)
                    .msg("질문 삭제 성공")
                    .status("200")
                    .token(memberIdx)
                    .build();

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            ResDto response = ResDto.builder()
                    .result(false)
                    .msg("질문 삭제 실패")
                    .data(null)
                    .status("400")
                    .errorType(e.getMessage())
                    .token(memberIdx)
                    .build();

            return ResponseEntity.status(400).body(response);
        }
    }

    // 검색 기능 - title, content, tag
    // GET /api/questions/search
    // 예시 ) 제목 투표순 정렬
    // GET /api/questions/search?title=example&sortBy=voteSum&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<?> searchQuestions(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        Page<QuestionDto> questionsPage = questionSearchService.searchQuestions(title, content, tag, name, page, size, sortBy);
        if (questionsPage.isEmpty()) {
            ResDto response = ResDto.builder()
                    .result(false)
                    .msg("검색 결과가 없습니다.")
                    .data(null)
                    .status("404")
                    .errorType("No Results Found")
                    .token(null)
                    .build();

            return ResponseEntity.status(404).body(response);
        }

        ResDto response = ResDto.builder()
                .result(true)
                .msg("검색 결과가 있습니다.")
                .data(questionsPage)
                .status("200")
                .token(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
