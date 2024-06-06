// package com.twat.detalks.question.entity;
//
// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
//
// import java.util.List;
//
// @Entity
// @Table(name = "members")
// @Getter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class MemberEntity {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name="member_idx", nullable = false)
//     private Long memberIdx;
//
//     @Column(name = "member_name", nullable = false)
//     private String memberName;
//
//     @Column(name = "member_email", nullable = false)
//     private String memberEmail;
//
//     @OneToMany(mappedBy = "member_idx", cascade = CascadeType.ALL, orphanRemoval = true)
//     private List<QuestionEntity> questions;
// }
