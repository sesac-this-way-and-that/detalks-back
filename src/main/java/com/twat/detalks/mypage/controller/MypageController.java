package com.twat.detalks.mypage.controller;

import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.service.AnswerService;
import com.twat.detalks.mypage.dto.AnswerListDto;
import com.twat.detalks.mypage.dto.QuestionAnswerListDto;
import com.twat.detalks.mypage.dto.QuestionListDto;
import com.twat.detalks.mypage.service.AnswerListService;
import com.twat.detalks.mypage.service.QuestionAnswerListService;
import com.twat.detalks.mypage.service.QuestionListService;
import com.twat.detalks.question.dto.ResErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<QuestionListDto>> getQuestionsByMemberId(@AuthenticationPrincipal String memberIdx) {
        Long memberId = Long.parseLong(memberIdx);
        List<QuestionListDto> questions = questionListService.getQuestionsByMemberId(memberId);
        return ResponseEntity.ok(questions);
    }

    // 특정 회원의 답변 리스트 조회
    // GET /api/mypage/{memberIdx}/answers
    @GetMapping("/{memberIdx}/answers")
    public ResponseEntity<List<AnswerListDto>> getAnswersByMemberId(@AuthenticationPrincipal String memberIdx) {
        Long memberId = Long.parseLong(memberIdx);
        List<AnswerListDto> answers = answerListService.getAnswersByMemberId(memberId);
        return ResponseEntity.ok(answers);
    }

    // (최신순) 특정 회원의 활동 리스트 조회
    // GET /api/mypage/{memberIdx}/activities/recent
    @GetMapping("/{memberIdx}/activities/recent")
    public ResponseEntity<List<QuestionAnswerListDto>> getRecentQuestionsAndAnswersByMemberId(@AuthenticationPrincipal String memberIdx) {
        Long memberId = Long.parseLong(memberIdx);
        List<QuestionAnswerListDto> questionAnswerList = questionAnswerListService.getQuestionsAndAnswersByMemberIdOrderByCreatedAtDesc(memberId);
        return ResponseEntity.ok(questionAnswerList);
    }

    // (투표순) 특정 회원의 활동 리스트 조회
    // GET /api/mypage/{memberIdx}/activities/top-votes
    @GetMapping("/{memberIdx}/activities/top-votes")
    public ResponseEntity<List<QuestionAnswerListDto>> getTopVotedQuestionsAndAnswersByMemberId(@AuthenticationPrincipal String memberIdx) {
        Long memberId = Long.parseLong(memberIdx);
        List<QuestionAnswerListDto> questionAnswerList = questionAnswerListService.getQuestionsAndAnswersByMemberIdOrderByVoteCountDesc(memberId);
        return ResponseEntity.ok(questionAnswerList);
    }
}
