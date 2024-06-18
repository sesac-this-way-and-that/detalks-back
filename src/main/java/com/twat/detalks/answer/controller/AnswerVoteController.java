package com.twat.detalks.answer.controller;

import com.twat.detalks.answer.dto.AnswerVoteDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.answer.repository.AnswerVoteRepository;
import com.twat.detalks.answer.service.AnswerVoteService;
import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.question.dto.ResErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/votes")
@Slf4j
public class AnswerVoteController {
    @Autowired
    private AnswerVoteService answerVoteService;

    @Autowired
    private AnswerRepositroy answerRepositroy;

    @Autowired
    private AnswerVoteRepository answerVoteRepository;

    // 답변 투표
    // POST /api/votes/answer/{answerId}
    @PostMapping("/answer/{answerId}")
    public ResponseEntity<?> voteAnswer(
            @PathVariable Long answerId,
            @RequestBody Map<String, Boolean> requestBody,
            @AuthenticationPrincipal CustomUserDetail user) {

        Boolean voteState = requestBody.get("voteState");
        String memberIdx = user.getUserIdx();
        log.warn("aaaaa : {}", memberIdx);
        log.warn("vvvv : {}", voteState);
        try {
            answerVoteService.addVote(answerId, Long.parseLong(memberIdx), voteState);

            AnswerEntity answer = answerRepositroy.findById(answerId)
                    .orElseThrow(() -> new RuntimeException("답변을 찾을 수 없습니다."));

            List<AnswerVoteDto> answerVoteList = answerVoteRepository.findByAnswer_AnswerId(answerId).stream()
                    .map(vote -> AnswerVoteDto.builder()
                            .voteId(vote.getVoteId())
                            .voteState(vote.getVoteState())
                            .memberIdx(vote.getMember().getMemberIdx().toString())
                            .build())
                    .collect(Collectors.toList());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("answerId", answer.getAnswerId());
            responseData.put("answerContent", answer.getAnswerContent());
            responseData.put("createdAt", answer.getCreatedAt());
            responseData.put("modifiedAt", answer.getModifiedAt());
            responseData.put("answerState", answer.getAnswerState());
            responseData.put("voteCount", answer.getVoteCount());
            responseData.put("isSelected", answer.getIsSelected());
            responseData.put("answerVoteList", answerVoteList);

            // String msg = BeVoteState ? "답변에 찬성했습니다." : "답변에 반대 했습니다.";
            String msg = voteState ? "답변에 찬성했습니다." : "답변에 반대 했습니다.";

            ResDto response = ResDto.builder()
                    .result(true)
                    .data(responseData)
                    .msg(msg)
                    .status("200")
                    .token(user.getUserIdx())
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
