package com.shop.service;

import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.domain.item.entity.Item;
import com.shop.domain.item.repository.ItemRepository;
import com.shop.domain.member.entity.Member;
import com.shop.domain.member.repository.MemberRepository;
import com.shop.domain.order.entity.Order;
import com.shop.domain.order.repository.OrderRepository;
import com.shop.domain.orderitem.entity.OrderItem;
import com.shop.web.controller.dto.request.OrderRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
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
        Assertions.assertEquals(totalPrice, order.getTotalPrice());
    }
}
