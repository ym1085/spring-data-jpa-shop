package com.shop.domain.member.entity;

import com.shop.domain.BaseEntity;
import com.shop.domain.member.constant.Role;
import com.shop.web.controller.dto.MemberFormRequestDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@ToString
@Getter
@Table(name = "member")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String userName;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String userName, String email, String password, String address, Role role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    /**
     * Member Entity를 생성하기 위한 메서드, Member Entity에 회원 생성 로직을 만들어서 관리하면
     * 코드가 변경되도 한 군데만 수정하면 되는 이점이 있음
     * @param memberFormRequestDto
     * @param passwordEncoder
     * @return Member
     */
    public static Member createMember(MemberFormRequestDto memberFormRequestDto,
                                      PasswordEncoder passwordEncoder) {
        return Member.builder()
                .userName(memberFormRequestDto.getUserName())
                .email(memberFormRequestDto.getEmail())
                .address(memberFormRequestDto.getAddress())
                .password(passwordEncoder.encode(memberFormRequestDto.getPassword()))
//                .role(Role.USER)
                .role(Role.ADMIN) // ADMIN 테스트
                .build();
    }
}
