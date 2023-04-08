package com.shop.domain.order.entity;

import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.domain.item.entity.Item;
import com.shop.domain.item.repository.ItemRepository;
import com.shop.domain.member.entity.Member;
import com.shop.domain.member.repository.MemberRepository;
import com.shop.domain.order.repository.OrderRepository;
import com.shop.domain.orderitem.entity.OrderItem;
import com.shop.domain.orderitem.repository.OrderItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    /* 상품 생성 */
    public Item createItem() {
        return Item.builder()
                .itemName("테스트 상품")
                .price(10000)
                .itemDetail("상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .regDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    /* 주문 생성 */
    public Order createOrder() {
        Order order = new Order(); // 깡통

        for (int i = 0; i < 2; i++) {
            Item item = this.createItem(); // 3개의 Item 생성
            Item savedItem = itemRepository.save(item); // Item 테이블에 저장

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
//    @Rollback(value = false)
    void persistenceTest() throws Exception {
        //given
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = this.createItem(); // 3개의 Item 생성
            Item savedItem = itemRepository.save(item); // Item 테이블에 저장

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        //when
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    void orphanRemovalTest() throws Exception {
        //given
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();

        //when
        //then
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    void lazyLoadingTest() throws Exception {
        //given
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        //when
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        //then
        System.out.println("Order class : " + orderItem.getOrder().getClass());
    }
}
