package com.shop.domain.orderitem.entity;

import com.shop.domain.BaseEntity;
import com.shop.domain.item.entity.Item;
import com.shop.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "order_item")
@Entity
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    /* xxToOne은 지연 로딩 전략을 사용하는것이 좋음, n + 1 문제 방지 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문할 총 상품의 가격
    private int count; // 주문할 상품의 수량

    /*private LocalDateTime regDate;
    private LocalDateTime updateDate;*/

    /* 객체 생성시 필요한 정보를 OrderItem 객체에 담아 반환 */
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderPrice(item.getPrice()); // 현 시간 기준 상품 가격을 주문 가격으로 셋팅,
        orderItem.setCount(count); // 주문할 주문 수량 셋팅
        orderItem.setItem(item); // 주문할 상품 셋팅

        item.removeItemStock(count); // item.removeItemStock( '유저가 주문한 상품의 갯수' ) 를 넘긴다
        return orderItem;
    }

    /* 상품 주문 가격, 주문 수량을 곱하여 해당 상품을 주문한 총 가격을 계산하는 함수 */
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}
