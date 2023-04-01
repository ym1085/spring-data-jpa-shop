package com.shop.config.auth;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @EnableWebSecurity
 * WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 선언 시 SpringSecurityFilterChain이 자동으로 포함됨
 * WebSecurityConfigurerAdapter를 상속 후 메서드 오버라이딩을 통해 보안 설정 커스텀 가능
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    /**
     * http 요청에 대한 보안 설정
     * - 페이지 접근 권한 설정, 로그인 페이지 설정, 로그아웃 메서드 등에 대한 설정
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/member/login") // Client -> HTTP 요청 -> 컨트롤러(로그인 페이지 URL 설정)
                    .defaultSuccessUrl("/")     // 로그인 성공 시 이동할 URL 지정
                    .usernameParameter("email") // 로그인시 사용할 파라미터 이름으로 email 지정
                    .failureUrl("/member/login/error") // 로그인 실패 시 이동할 URL 설정
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 URL 설정
                    .logoutSuccessUrl("/"); // 로그아웃 성공 시 이동할 URL 지정
    }

    /**
     * 비밀번호를 DB에 그대로 저장하는 경우, DB가 해킹 당하면 보안 이슈가 발생함
     * BCryptPasswordEncoder의 해시 함수를 이용하여 비밀번호 암호화 후 저장
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security에서 인증은 AuthenticationManager를 통해 이루어지며 AuthenticationManagerBuilder가
     * AuthenticationManager를 생성한다. userDetailService를 구현하고 있는 객체로 memberService를 지정
     * 해주며, 비밀번호 암호화를 위해 passwordEncoder를 지정한다
     *
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}

