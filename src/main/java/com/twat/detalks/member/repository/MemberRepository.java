package com.twat.detalks.member.repository;

import com.twat.detalks.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    boolean existsByMemberEmail (String email);
    boolean existsByMemberName(String name);
    Optional<MemberEntity> findByMemberEmail(String email);
    MemberEntity findByMemberPwd(String socialMemberPwd);
}
