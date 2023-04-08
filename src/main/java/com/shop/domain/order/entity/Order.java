package com.shop.domain.order.entity;

import com.shop.domain.member.entity.Member;
import com.shop.domain.order.constant.OrderStatus;
import com.shop.domain.orderitem.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Table(name = "orders") // 정렬 할 때 order 사용하기에, orders로 지정..
@Entity // JPA에 의해 관리가 되도록 지정, DB 연결 등등...
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    /* 한명의 회원(1)은 여러개의 주문(N)을 할 수 있다 */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();

    private LocalDateTime orderDate; // 주문 날짜

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
