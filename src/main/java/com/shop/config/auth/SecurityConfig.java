package com.shop.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @EnableWebSecurity
 * WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 선언 시 SpringSecurityFilterChain이 자동으로 포함됨
 * WebSecurityConfigurerAdapter를 상속 후 메서드 오버라이딩을 통해 보안 설정 커스텀 가능
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * http 요청에 대한 보안 설정
     * - 페이지 접근 권한 설정, 로그인 페이지 설정, 로그아웃 메서드 등에 대한 설정
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

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
}

