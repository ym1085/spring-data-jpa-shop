package com.shop.service;

import com.shop.domain.item.entity.Item;
import com.shop.domain.item.repository.ItemRepository;
import com.shop.domain.itemimg.entity.ItemImg;
import com.shop.domain.itemimg.repository.ItemImgRepository;
import com.shop.domain.member.entity.Member;
import com.shop.domain.member.repository.MemberRepository;
import com.shop.domain.order.entity.Order;
import com.shop.domain.order.repository.OrderRepository;
import com.shop.domain.orderitem.entity.OrderItem;
import com.shop.web.controller.dto.request.OrderRequestDto;
import com.shop.web.controller.dto.response.OrderItemHistResponseDto;
import com.shop.web.controller.dto.response.OrderItemResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    /**
     * 상품 주문
     * @param orderRequestDto itemId: 상품 ID, count: 주문 수량 전달
     * @param email 상품 주문을 수행하는 회원의 이메일
     * @return 주문 완료된 orderId 반환
     */
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

    /**
     * 회원 ID에 해당하는 주문 이력 조회
     * @param email 회원 ID
     * @param pageable 전체 주문 이력 중 보여줄 페이지 개수
     * @return 주문 이력 정보
     */
    @Transactional(readOnly = true)
    public Page<OrderItemHistResponseDto> getOrderList(String email, Pageable pageable) {
        log.debug("email = {}, pageable = {}", email, pageable);
        List<Order> orders = orderRepository.findOrders(email, pageable); // 유저의 ID와 페이징 조건을 이용해 주문 목록 조회,  // order의 size만큼 query가 실행되는 성능 이슈 발생
        Long totalCount = orderRepository.countOrder(email); // 유저의 주문 총 개수를 구한다

        List<OrderItemHistResponseDto> orderHitsDtoList = new ArrayList<>();
        for (Order order : orders) { // 전체 주문 목록에서 단건 주문 결과를 뽑는다
            OrderItemHistResponseDto orderHitsDto = new OrderItemHistResponseDto(order); // orderId, orderDate, orderStatus 셋팅
            List<OrderItem> orderItems = order.getOrderItems(); // order의 size만큼 query가 실행되는 성능 이슈 발생
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y"); // 주문한 상품의 대표 이미지 조회
                OrderItemResponseDto orderItemRequestDto = new OrderItemResponseDto(orderItem, itemImg.getImgUrl());
                orderHitsDto.addOrderItemDto(orderItemRequestDto);
            }
            orderHitsDtoList.add(orderHitsDto);
        }
        return new PageImpl<>(orderHitsDtoList, pageable, totalCount); // 페이지 구현 객체 생성 후 반환
    }

    /**
     * 현재 로그인한 유저와, 주문을 한 유저의 Email을 비교하여 주문 취소 진행 여부를 반환
     * @param orderId 주문 번호
     * @param email 로그인한 유저의 이메일
     * @return
     */
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        Member currentMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException());

        Member orderedMember = order.getMember();
        if (!StringUtils.equals(currentMember.getEmail(), orderedMember.getEmail())) {
            // 현재 로그인 한 유저와 주문한 유저가 동일 하지 않다면 return
            return false;
        }
        return true;
    }

    /**
     * 주문 번호를 기반으로 주문 취소를 수행
     * @param orderId 주문 번호
     */
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException()); // orderId에 해당하는 주문 정보
        order.cancelOrder();
    }
}
