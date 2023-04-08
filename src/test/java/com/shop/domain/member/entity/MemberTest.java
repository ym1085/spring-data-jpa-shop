package com.shop.domain.member.entity;

import com.shop.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER") // spring security가 적요된 곳을 효율적으로 테스트
    void auditingTest() throws Exception {
        //given
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush(); // 쓰기 지연소 SQL -> DB 반영
        em.clear(); // 영속성 컨텍스 비워버리면, DB에서 값을 가져오겠지?

        //when
        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);
        //then
        System.out.println("regDate => " + member.getRegDate());
        System.out.println("updateDate => " + member.getUpdateDate());
        System.out.println("created Member => " + member.getCreatedBy());
        System.out.println("modify member => " + member.getModifiedBy());
    }
}
