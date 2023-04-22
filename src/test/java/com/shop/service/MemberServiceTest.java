package com.shop.service;

import com.shop.domain.member.entity.Member;
import com.shop.web.controller.dto.request.MemberFormRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional // https://okky.kr/questions/873387
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Member Entity에 존재하는 createMember 메서드를 사용하여 회원 테스트 데이터 초기화
     * @return 회원 가입 된 유저를 반환 한다
     */
    public Member createMember() {
        return Member.createMember(MemberFormRequestDto.builder()
                .userName("한지민")
                .email("test@gmail.com")
                .address("서울시 마포구 합정동")
                .password("12345678")
                .build(), passwordEncoder);
    }

    // [assertEquals vs assertThat](https://jongmin92.github.io/2020/03/31/Java/use-assertthat/)

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest() throws Exception {
        //given
        Member member = createMember();

        //when
        Member savedMember = memberService.join(member);

        //then
        assertThat(member).isNotNull();
        assertThat(member.getUserName()).isEqualTo(savedMember.getUserName());
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getAddress()).isEqualTo(savedMember.getAddress());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 테스트")
    void validateDuplicateMemberTest() throws Exception {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.join(member1);

        //when
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });

        //then
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
//        assertEquals("이 가입된 회원입니다.", e.getMessage()); // fail test case
    }

}
