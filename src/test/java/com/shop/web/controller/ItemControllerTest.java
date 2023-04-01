package com.shop.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

// @AutoConfigureMockMvc : Mock 테스트시 필요한 의존성을 제공하는 어노테이션
// @MockMvc : 애플리케이션 서버를 구동하지 않고도 'Spring MVC 동작을 재현'할 수 있는 모의(가짜) 객체
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest // Integration testing
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 어드민 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN") // 회원 이름이 admin이고 role이 ADMIN인 유저가 로그인된 상태로 테스트 할 수 있게 해주는 어노테이션
    void itemFormTestByAdmin() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        //then
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 유저 권한 테스트")
    @WithMockUser(username = "user", roles = "USER")
    void itemFormTestByUser() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        //then
    }
}
