package com.shop.service;

import com.shop.domain.item.entity.Item;
import com.shop.domain.item.repository.ItemRepository;
import com.shop.domain.member.entity.Member;
import com.shop.domain.member.repository.MemberRepository;
import com.shop.domain.order.entity.Order;
import com.shop.domain.order.repository.OrderRepository;
import com.shop.domain.orderitem.entity.OrderItem;
import com.shop.web.controller.dto.request.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderRequestDto orderRequestDto, String email) {
        /* item id를 기반으로 해당 id에 해당하는 상품을 조회 한다 */
        Item item = itemRepository.findById(orderRequestDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        /* email를 기반으로 상품을 주문하는 사용자의 정보를 가져온다 */
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        /* 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티 생성 */
        OrderItem orderItem = OrderItem.createOrderItem(item, orderRequestDto.getCount());
        orderItemList.add(orderItem);

        /* 회원 정보와  */
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
