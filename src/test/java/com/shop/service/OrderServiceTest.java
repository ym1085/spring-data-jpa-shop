package com.shop.service;

import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.domain.item.entity.Item;
import com.shop.domain.item.repository.ItemRepository;
import com.shop.domain.member.entity.Member;
import com.shop.domain.member.repository.MemberRepository;
import com.shop.domain.order.constant.OrderStatus;
import com.shop.domain.order.entity.Order;
import com.shop.domain.order.repository.OrderRepository;
import com.shop.domain.orderitem.entity.OrderItem;
import com.shop.web.controller.dto.request.OrderRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setItemDetail("테스트 상품 상세 설명");
        item.setPrice(10000);
        item.setItemSellStatus(ItemSellStatus.SELL); // 판매중
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("상품 주문 테스트")
    void order() throws Exception {
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCount(10); // 주문할 상품 수량
        orderRequestDto.setItemId(item.getId()); // 주문할 상품

        //when
        Long orderId = orderService.order(orderRequestDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderRequestDto.getCount() * item.getPrice();

        //then
        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void cancelOrder() {
        //given
        //Item(?), Order:orderId(N), Member:email(1)
        Item item = saveItem(); // 상품 데이터 생성
        Member member = saveMember(); // 유저 데이터 생성

        OrderRequestDto orderRequestDto = new OrderRequestDto();

        // 상품 주문 시 사용 -> itemId 기반 상품(Item) 조회 + Member 조회 + OrderItem 생성(count, item.getPrice()) // 상품 재고 차감
        orderRequestDto.setItemId(item.getId()); // 상품 ID
        orderRequestDto.setCount(10); // 주문할 상품의 수량

        // 주문 등록 -> 기존 100개 -> 101개

        //when
        Long orderId = orderService.order(orderRequestDto, member.getEmail()); // 상품 주문 먼저 한다
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException()); // 주문 등록 후 해당 ID 기반 조회

        // 주문 취소 수행
        orderService.cancelOrder(order.getId());

        // 주문 취소 -> 기존 101개 -> 100개

        //then
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus()); // Order 객체는 orderDate, orderStatus 필드 가지고 있음
        assertEquals(100, item.getStockNumber());
    }
}
