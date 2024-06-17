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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
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
    public ResponseEntity<?> getQuestionsByMemberId(
            @PathVariable String memberIdx,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
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
    public ResponseEntity<?> getAnswersByMemberId(
            @PathVariable String memberIdx,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
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
    public ResponseEntity<?> getRecentQuestionsAndAnswersByMemberId(@PathVariable String memberIdx) {
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
    public ResponseEntity<?> getTopVotedQuestionsAndAnswersByMemberId(@PathVariable String memberIdx) {
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
