package com.shop.domain.cart.entity;

import com.shop.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

/*
    연관 관계
    -> Cart(1) : Member(1)

    @JoinColumn
    -> 외래키를 매핑할 때 사용되는 어노테이션
    -> Team(1) : Member(N) 관계가 있다고 가정을 해보자
        -> Member 엔티티에 Team 객체를 맴버 변수로 선언 @JoinColumn(name = "team_id")
        -> Member 엔티티의 Team team 변수를 team_id 외래키로 지정 하겠다는 의미
*/
@ToString
@Getter
@Setter // Temp
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart")
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // 회원 엔티티와 1:1 매핑
    @JoinColumn(name = "member_id") // 매핑할 외래키 지정, name 속성에 매핑할 외래키의 이름을 설정한다
    private Member member;

}
