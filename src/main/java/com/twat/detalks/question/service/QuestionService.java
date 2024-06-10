package com.twat.detalks.question.service;

import com.twat.detalks.answer.dto.AnswerDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.question.dto.MemberQuestionDto;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.security.TokenProvider;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.tag.entity.QuestionTagEntity;
import com.twat.detalks.tag.entity.TagEntity;
import com.twat.detalks.tag.repository.QuestionTagRepository;
import com.twat.detalks.tag.repository.TagRepository;
import com.twat.detalks.tag.service.TagService;
import com.twat.detalks.question.repository.QuestionVoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionTagRepository questionTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionVoteRepository voteRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TokenProvider tokenProvider;

    // 질문 리스트 조회
    public List<QuestionDto> getQuestions() {
        return questionRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 상세 질문 조회
    public QuestionDto getQuestionById(Long questionId) {
        QuestionEntity findQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        // 조회수 업데이트
        questionRepository.updateViewCount(questionId);

        // 좋아요와 싫어요 수 계산
        int likeCount = voteRepository.countByQuestions_QuestionIdAndVoteState(questionId, true);
        int dislikeCount = voteRepository.countByQuestions_QuestionIdAndVoteState(questionId, false);

        // 질문에 대한 총 투표 수 업데이트
        int voteSum = likeCount - dislikeCount;

        findQuestion.setVoteCount(voteSum);
        questionRepository.save(findQuestion);

        return convertToDTO(findQuestion);
    }



    // 질문 생성
    public QuestionDto createQuestion(Long memberIdx, QuestionCreateDto questionCreateDto) {
        MemberEntity member = memberRepository.findById(memberIdx)
        // MemberEntity member = memberRepository.findById(Long.parseLong(memberIdx))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        // 새 질문 생성
        QuestionEntity newQuestion = QuestionEntity.builder()
                .questionTitle(questionCreateDto.getQuestionTitle())
                .questionContent(questionCreateDto.getQuestionContent())
                .members(member)
                .build();

        questionRepository.save(newQuestion);

        // 태그 저장
        if (questionCreateDto.getTagNames() != null) {
            // 태그명 추가
            List<TagEntity> tagsList = questionCreateDto.getTagNames().stream()
                    .distinct()
                    .map(tagName -> tagRepository.findByTagName(tagName)
                            .orElseGet(() -> tagRepository.save(new TagEntity(tagName))))
                    .collect(Collectors.toList());

            // 질문에 태그 추가
            List<QuestionTagEntity> questionTagsList = tagsList.stream()
                    .map(tag -> QuestionTagEntity.builder()
                            .question(newQuestion)
                            .tags(tag)
                            .build())
                    .collect(Collectors.toList());

            questionTagRepository.saveAll(questionTagsList);
        }

        return convertToDTO(newQuestion);
    }

    // 질문 수정
    public QuestionDto updateQuestion(Long questionId, QuestionCreateDto questionCreateDto, Long memberIdx) {
        // 질문 존재 여부 확인
        // QuestionEntity existingQuestion = questionRepository.findById(questionId)
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        // 수정 권한 확인
        if (!existingQuestion.getMembers().getMemberIdx().equals(memberIdx)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 질문 수정
        existingQuestion.setQuestionTitle(questionCreateDto.getQuestionTitle());
        existingQuestion.setQuestionContent(questionCreateDto.getQuestionContent());

        // 태그 수정 및 삭제
        questionTagRepository.deleteByQuestion(existingQuestion);

        if (questionCreateDto.getTagNames() != null) {
            List<TagEntity> tagsList = questionCreateDto.getTagNames().stream()
                    .distinct()
                    .map(tagName -> tagRepository.findByTagName(tagName)
                            .orElseGet(() -> tagRepository.save(new TagEntity(tagName))))
                    .collect(Collectors.toList());

            List<QuestionTagEntity> questionTagsList = tagsList.stream()
                    .map(tag -> QuestionTagEntity.builder()
                            .question(existingQuestion)
                            .tags(tag)
                            .build())
                    .collect(Collectors.toList());

            questionTagRepository.saveAll(questionTagsList);
        }

        return convertToDTO(questionRepository.save(existingQuestion));
    }

    // 질문 삭제
    public void deleteQuestion(Long questionId, Long memberIdx) {
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        if (!existingQuestion.getMembers().getMemberIdx().equals(memberIdx)) {
        // if (!existingQuestion.getMembers().getMemberIdx().equals(Long.parseLong(memberIdx))) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        questionTagRepository.deleteByQuestion(existingQuestion);
        questionRepository.delete(existingQuestion);
    }


    // 질문 entity를 dto로 변환
    private QuestionDto convertToDTO(QuestionEntity questionEntity) {
        // 답변 정보
        List<AnswerDto> answerDtoList = (questionEntity.getAnswerList() != null ? questionEntity.getAnswerList() : new ArrayList<AnswerEntity>()).stream()
                .map(answer -> AnswerDto.builder()
                        .answerId(answer.getAnswerId())
                        .answerContent(answer.getAnswerContent())
                        .createdAt(answer.getCreatedAt())
                        .modifiedAt(answer.getModifiedAt())
                        .answerState(answer.getAnswerState())
                        .voteCount(answer.getVoteCount())
                        .isSelected(answer.getIsSelected())
                        .author(new MemberQuestionDto(
                                answer.getMembers().getMemberIdx(),
                                answer.getMembers().getMemberName()))
                        .build())
                .collect(Collectors.toList());

        // 태그명
        List<String> tagNameList = questionTagRepository.findByQuestion(questionEntity).stream()
                .map(questionTags -> questionTags.getTags().getTagName())
                .collect(Collectors.toList());

        // 질문 정보
        return QuestionDto.builder()
                .questionId(questionEntity.getQuestionId())
                .questionTitle(questionEntity.getQuestionTitle())
                .questionContent(questionEntity.getQuestionContent())
                .createdAt(questionEntity.getCreatedAt())
                .modifiedAt(questionEntity.getModifiedAt())
                .viewCount(questionEntity.getViewCount())
                .voteCount(questionEntity.getVoteCount())
                .questionState(questionEntity.getQuestionState())
                .isSolved(questionEntity.getIsSolved())
                .author(new MemberQuestionDto(
                        questionEntity.getMembers().getMemberIdx(),
                        questionEntity.getMembers().getMemberName()))
                .answerList(answerDtoList)
                .tagNameList(tagNameList)
                .build();
    }
}
