package com.shop.domain.cartitem.entity;

import com.shop.domain.cart.entity.Cart;
import com.shop.domain.item.entity.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@Setter // 실무에서는 사양 지양
@Table(name = "cart_item")
@Entity
public class CartItem {

    /**
     * 장바구니에는 고객이 관심 있거나 나중에 사려는 "상품"을 담아 둘 것이다.
     * 1. 회원(1)이 상품(N)을 여러개 담을 수 있다
     * 2. 하나의 장바구니에는 여러개의 상품이 들어갈 수 있다
     * 3. 같은 상품을 여러 개 주문할 수도 있기에 몇 개를 담아 줄 것인지도 설정해야 함
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_item_id")
    private Long id;

    /* 하나의 "장바구니"에는 여러개의 "상품"을 담을 수 있기에 @ManyToOne 어노테이션 사용 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /*
        장바구니에 담을 상품의 정보를 알아야 하기에 상품 엔티티 매핑, 하나의 상품은 여러 장바구니의 장바구니 상품으로
        담길 수 있기에 마찬가지로 @ManyToOne 어노테이션 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
}
