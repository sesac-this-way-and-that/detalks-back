package com.twat.detalks.question;

import com.twat.detalks.member.entity.MemberEntity;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.tag.entity.QuestionTagEntity;
import com.twat.detalks.tag.entity.TagEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

// DB에서 데이터 조회시 동적 쿼리를 작성 가능한 것
public class QuestionSpecification {
    public static Specification<QuestionEntity> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("questionTitle"), "%" + title + "%");
    }

    public static Specification<QuestionEntity> hasContent(String content) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("questionContent"), "%" + content + "%");
    }

    public static Specification<QuestionEntity> hasTag(String tagName) {
        return (root, query, criteriaBuilder) -> {
            Join<QuestionEntity, QuestionTagEntity> questionTagJoin = root.join("questionTagList", JoinType.INNER);
            Join<QuestionTagEntity, TagEntity> tagJoin = questionTagJoin.join("tags", JoinType.INNER);
            return criteriaBuilder.equal(tagJoin.get("tagName"), tagName);
        };
    }

    // 회원이름으로 질문 조회
    public static Specification<QuestionEntity> hasName(String memberName) {
        return (root, query, criteriaBuilder) -> {
            Join<QuestionEntity, MemberEntity> memberJoin = root.join("members", JoinType.INNER);
            return criteriaBuilder.equal(memberJoin.get("memberName"), memberName);
        };
    }
}
