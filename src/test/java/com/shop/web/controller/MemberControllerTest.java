package com.shop.web.controller;

import com.shop.domain.member.entity.Member;
import com.shop.service.MemberService;
import com.shop.web.controller.dto.MemberFormRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * classpath 복기
 *
 * Spring Boot에서 "classpath"는 애플리케이션을 실행할 때
 * '클래스 파일', '프로퍼티 파일', '리소스 파일' 등을 찾을 때 사용되는 경로를 나타냅니다.
 * -> class 파일 포함하는 bin 디렉토리
 * -> src/main/resources 디렉토리의 리소스 파일
 * -> Maven의 경우 Maven Dependency에 있는 라이브러리 파일
 */

// @AutoConfigureMockMvc : Mock 테스트시 필요한 의존성을 제공하는 어노테이션
// @MockMvc : 애플리케이션 서버를 구동하지 않고도 'Spring MVC 동작을 재현'할 수 있는 모의(가짜) 객체
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest // Integration testing
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password) {
        // return에 다 넣는 것보다, 분리 하는게 조금 더 직관적인 것 같음
        Member member = Member.createMember(MemberFormRequestDto.builder()
                .userName("홍길동")
                .address("서울시 마포구 합정동")
                .email(email)
                .password(password)
                .build(), passwordEncoder);

        return memberService.join(member); // 회원 가입 진행
    }

    @Test
    @DisplayName("회원 로그인 성공 테스트")
    void loginTest() throws Exception {
        //given
        String email = "test@email.com";
        String password = "test1234!";
        Member member = createMember(email, password);

        //when
//        mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin().userParameter("email"))
        ResultActions result = mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/member/login")
                        .user(email)
                        .password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andDo(print());

        //then
        System.out.println("result => " + result);
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginFailTest() throws Exception {
        //given
        String email = "test@email.com";
        String password = "test1234!";
        Member member = createMember(email, password);

        //when
        ResultActions result = mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/member/login")
                        .user(email)
                        .password("12345678"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
                .andDo(print());

        //then
    }
}
