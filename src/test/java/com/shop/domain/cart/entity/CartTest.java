package com.shop.domain.cart.entity;

import com.shop.domain.cart.repository.CartRepository;
import com.shop.domain.member.entity.Member;
import com.shop.domain.member.repository.MemberRepository;
import com.shop.web.controller.dto.MemberFormRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // EntityManager를 bean로 주입할 때 사용
    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        return Member.createMember(MemberFormRequestDto.builder()
                .userName("한지민")
                .email("test@gmail.com")
                .address("서울시 마포구 합정동")
                .password("12345678")
                .build(), passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    void test() throws Exception {
        //given
        Member member = createMember(); // 회원 더미 데이터 생성

        //when
        Member savedMember = memberRepository.save(member); // 회원 데이터 저장

        Cart cart = new Cart(); // Cart(N) : Member(1) -> 요런 느낌으로 생각하면 될 듯
        cart.setMember(member);
        Cart savedCart = cartRepository.save(cart);

        // JPA는 영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날 때 flush() 를 호출하여
        // DB에 반영한다. 회원 엔티티와 장바구니 엔티티를 영속성 컨텍스트에 저장 후 엔티티 매니저로부터
        // 강제로 flush() 를 호출하여 DB에 반영한다.
        em.flush();

        // JPA는 영속성 컨텍스트로부터 엔티티를 조회 후 영속성 컨텍스트에 해당 엔티티가 없는 경우
        // DB를 조회한다. 실제 DB에서 장바구니 엔티티를 가져올 때 회원 엔티티도 가져오는지 테스트 하기 위해
        // 영속성 컨텍스트를 비운다
        em.clear();

        Cart cartList = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find Entity, id=" + cart.getId()));

        //then
        assertThat(cartList).isNotNull();
        assertThat(savedCart.getId()).isEqualTo(cartList.getId());
    }
}
