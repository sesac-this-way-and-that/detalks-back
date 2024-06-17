package com.twat.detalks.question.service;

import com.twat.detalks.answer.dto.AnswerDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.question.dto.MemberQuestionDto;
import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.dto.QuestionCreateDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.BookmarkRepository;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.member.repository.MemberRepository;
import com.twat.detalks.member.service.MemberService;
import com.twat.detalks.tag.entity.QuestionTagEntity;
import com.twat.detalks.tag.entity.TagEntity;
import com.twat.detalks.tag.repository.QuestionTagRepository;
import com.twat.detalks.tag.repository.TagRepository;
import com.twat.detalks.question.repository.QuestionVoteRepository;
import com.twat.detalks.tag.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

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
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TagService tagService;


    // 질문 리스트 조회
    public Page<QuestionDto> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable)
                .map(question -> convertToDTO(question, false));
    }

    // 답변이 없는 질문 리스트 조회
    public Page<QuestionDto> getQuestionsWithoutAnswers(Pageable pageable) {
        return questionRepository.findQuestionsWithoutAnswers(pageable)
                .map(question -> convertToDTO(question, false));
    }

    // 상세 질문 조회
    public QuestionDto getQuestionById(Long questionId, Long memberIdx) {
        QuestionEntity findQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        // 북마크 상태 업데이트
        Boolean bookmarkState = updateBookmarkState(memberIdx, questionId);

        // 조회수 업데이트
        questionRepository.updateViewCount(questionId);

        // 좋아요와 싫어요 수 계산
        int likeCount = voteRepository.countByQuestions_QuestionIdAndVoteState(questionId, true);
        int dislikeCount = voteRepository.countByQuestions_QuestionIdAndVoteState(questionId, false);

        // 질문에 대한 총 투표 수 업데이트
        int voteSum = likeCount - dislikeCount;

        findQuestion.setVoteCount(voteSum);
        questionRepository.save(findQuestion);

        return convertToDTO(findQuestion, bookmarkState);
    }

    // 질문 생성
    public QuestionDto createQuestion(String memberIdx, QuestionCreateDto questionCreateDto) throws NumberFormatException{
        /* 기존 코드
        // MemberEntity member = memberRepository.findById(memberIdx)
        //         .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
         */

        // 이미 멤버 서비스에 예외처리 포함된 메서드 구현되어 있음
        MemberEntity member = memberService.findByMemberId(memberIdx);

        // 이부분 만약 평판점수가 문자열로 들어온다면 예외 발생 가능성이 있음!!!!
        Integer questionRep = questionCreateDto.getQuestionRep();

        // 평판 점수가 설정되지 않은 경우 기본값 0으로 설정
        if (questionRep == null) {
            questionRep = 0;
        }

        // 이미 구현되어 있음
        // if (member.getMemberRep() <= questionRep) {
        //     throw new IllegalArgumentException("회원의 평판 점수가 충분하지 않습니다.");
        // }

        /* 기존 코드
        // 멤버 rep - 질문 rep
        // MemberEntity updatedMember = member.toBuilder()
        //         .memberRep(member.getMemberRep() - questionRep)
        //         .build();
        // memberRepository.save(updatedMember);
        */

        // 바운티 설정
        String bounty = String.valueOf(questionRep);
        memberService.setBounty(bounty,memberIdx);


        // 새 질문 생성
        QuestionEntity newQuestion = QuestionEntity.builder()
                .questionTitle(questionCreateDto.getQuestionTitle())
                .questionContent(questionCreateDto.getQuestionContent())
                .members(member)
                .questionRep(questionRep)
                .build();

        questionRepository.save(newQuestion);

        // 태그 저장
        if (questionCreateDto.getTagNames() != null && !questionCreateDto.getTagNames().isEmpty()) {
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

        return convertToDTO(newQuestion, false);
    }

    // 질문 수정
    public QuestionDto updateQuestion(Long questionId, QuestionCreateDto questionCreateDto, Long memberIdx) {
        // 질문 존재 여부 확인
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

        return convertToDTO(questionRepository.save(existingQuestion), false);
    }

    // 질문 삭제
    public void deleteQuestion(Long questionId, Long memberIdx) {
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));

        if (!existingQuestion.getMembers().getMemberIdx().equals(memberIdx)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        questionTagRepository.deleteByQuestion(existingQuestion);
        questionRepository.delete(existingQuestion);
    }

    // 회원 별로 질문 리스트 조회
    public List<QuestionEntity> getQuestionsByMember(Long memberIdx) {
        return questionRepository.findByMembers_MemberIdx(memberIdx);
    }


    // 상세 질문 조회 시 북마크 상태 업데이트
    public Boolean updateBookmarkState(Long memberIdx, Long questionId) {
        Boolean bookmarkState = false;
        if (memberIdx != null) {
            BookmarkEntity bookmark = bookmarkRepository.findByMember_MemberIdxAndQuestion_QuestionId(memberIdx, questionId)
                    .orElse(null);
            if (bookmark != null) {
                bookmarkState = bookmark.getBookmarkState();
            }
        }
        return bookmarkState;
    }

    // 질문 entity를 dto로 변환
    public QuestionDto convertToDTO(QuestionEntity questionEntity, Boolean bookmarkState) {
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
                .answerCount(answerDtoList.size())
                .tagNameList(tagNameList)
                .bookmarkState(bookmarkState)
                .questionRep(questionEntity.getQuestionRep())
                .build();
    }
}
