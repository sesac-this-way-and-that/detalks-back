package com.twat.detalks.mypage.controller;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.service.AnswerService;
import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.mypage.dto.AnswerListDto;
import com.twat.detalks.mypage.dto.QuestionAnswerListDto;
import com.twat.detalks.mypage.dto.QuestionListDto;
import com.twat.detalks.mypage.service.AnswerListService;
import com.twat.detalks.mypage.service.QuestionAnswerListService;
import com.twat.detalks.mypage.service.QuestionListService;
import com.twat.detalks.question.dto.ResErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@Tag(name = "마이페이지 API", description = "마이페이지 관련 정보 API")
public class MypageController {
    @Autowired
    private QuestionListService questionListService;

    @Autowired
    private AnswerListService answerListService;

    @Autowired
    private QuestionAnswerListService questionAnswerListService;

    // 특정 회원의 질문 리스트 조회
    // GET /api/mypage/{memberIdx}/questions
    @GetMapping("/{memberIdx}/questions")
    @Operation(summary = "특정 회원의 질문 리스트 조회")
    public ResponseEntity<?> getQuestionsByMemberId(
        @Parameter(name="memberIdx",description = "회원 아이디")
            @PathVariable String memberIdx,
        @Parameter(name="page",description = "페이지 기본값 [0]")
            @RequestParam(defaultValue = "0") int page,
        @Parameter(name="size",description = "사이즈 기본값 [10]")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(name="sortBy",description = "정렬 기본값 createdAt")
        @RequestParam(defaultValue = "createdAt") String sortBy) {
        Long memberId = Long.parseLong(memberIdx);
        Page<QuestionListDto> questions = questionListService.getQuestionsByMemberId(memberId, page, size, sortBy);
        ResDto response = ResDto.builder()
                .result(false)
                .msg("질문 조회 성공")
                .data(questions)
                .status("200")
                .token(memberIdx)
                .build();

        return ResponseEntity.ok(response);
    }

    // 특정 회원의 답변 리스트 조회
    // GET /api/mypage/{memberIdx}/answers
    @GetMapping("/{memberIdx}/answers")
    @Operation(summary = "특정 회원의 답변 리스트 조회")
    public ResponseEntity<?> getAnswersByMemberId(
        @Parameter(name="memberIdx",description = "회원 아이디")
        @PathVariable String memberIdx,
        @Parameter(name="page",description = "페이지 기본값 [0]")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(name="size",description = "사이즈 기본값 [10]")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(name="sortBy",description = "정렬 기본값 createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Long memberId = Long.parseLong(memberIdx);
        Page<AnswerListDto> answers = answerListService.getAnswersByMemberId(memberId, page, size, sortBy);
        ResDto response = ResDto.builder()
                .result(false)
                .msg("답변 조회 성공")
                .data(answers)
                .status("200")
                .token(memberIdx)
                .build();

        return ResponseEntity.ok(response);
    }

    // (최신순) 특정 회원의 활동 리스트 조회
    // GET /api/mypage/{memberIdx}/activities/recent
    @GetMapping("/{memberIdx}/activities/recent")
    @Operation(summary = "(최신순)특정 회원의 활동 리스트 조회")
    public ResponseEntity<?> getRecentQuestionsAndAnswersByMemberId(
        @Parameter(name="memberIdx",description = "회원 아이디")
        @PathVariable String memberIdx) {
        Long memberId = Long.parseLong(memberIdx);
        List<QuestionAnswerListDto> questionAnswerList = questionAnswerListService.getQuestionsAndAnswersByMemberIdOrderByCreatedAtDesc(memberId);
        ResDto response = ResDto.builder()
                .result(false)
                .msg("활동 리스트 조회 성공(최신순)")
                .data(questionAnswerList)
                .status("200")
                .token(memberIdx)
                .build();

        return ResponseEntity.ok(response);
    }

    // (투표순) 특정 회원의 활동 리스트 조회
    // GET /api/mypage/{memberIdx}/activities/top-votes
    @GetMapping("/{memberIdx}/activities/top-votes")
    @Operation(summary = "(투표순)특정 회원의 활동 리스트 조회")
    public ResponseEntity<?> getTopVotedQuestionsAndAnswersByMemberId(
        @Parameter(name="memberIdx",description = "회원 아이디")
        @PathVariable String memberIdx) {
        Long memberId = Long.parseLong(memberIdx);
        List<QuestionAnswerListDto> questionAnswerList = questionAnswerListService.getQuestionsAndAnswersByMemberIdOrderByVoteCountDesc(memberId);
        ResDto response = ResDto.builder()
                .result(false)
                .msg("활동 리스트 조회 성공(투표순)")
                .data(questionAnswerList)
                .status("200")
                .token(memberIdx)
                .build();

        return ResponseEntity.ok(response);
    }
}
