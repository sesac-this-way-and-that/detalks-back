package com.twat.detalks.mypage.dto;

import lombok.*;
import java.time.LocalDateTime;


@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerListDto {
        private Long id;
        private String titleOrContent;
        private Boolean isQuestion;
        private LocalDateTime createdAt;
        private int voteCount;
        // 여기서 만약 isSolved가 null값일 경우 답변이고, isSelected가 null 값일 경우 질문
        private Boolean isSolved;
        private Boolean isSelected;
}
