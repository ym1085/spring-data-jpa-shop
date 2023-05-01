package com.shop.domain.order.entity;

import com.shop.domain.BaseEntity;
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
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    /* 한명의 회원(1)은 여러개의 주문(N)을 할 수 있다 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate; // 주문 날짜

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    /*private LocalDateTime regDate;
    private LocalDateTime updateDate;*/

    /* 양방향 연관관계 시, 연관관계 편의 메서드 사용 // 단방향 관계에서도 사용은 할 수 있음
       연관관계 편의 메서드는 1 : N 관계가 존재할 때 '1' 쪽에서 주로 셋팅 해주는듯?
    */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /* 객체 생성시 필요한 정보를 Order 객체에 담아 반환 */
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList) { // 장바구니에서는 여러번 상품 주문이 가능하기에 다음과 같이 작성
            order.addOrderItem(orderItem); // 연관관계 편의 메서드 호출
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /* 총 주문 금액을 구하는 메서드 */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
