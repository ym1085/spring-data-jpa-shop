package com.shop.domain.member.repository;

import com.shop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /* 이메일 기반 중복 회원 조회 */
    Member findByEmail(String email);

}
