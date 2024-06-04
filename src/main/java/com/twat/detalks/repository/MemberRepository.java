package com.twat.detalks.repository;

import com.twat.detalks.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    boolean existsByMemberEmail (String email);
    boolean existsByMemberName(String name);
    MemberEntity findByMemberEmail(String email);

}
