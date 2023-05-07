package com.shop.web.controller.dto.response;

import com.shop.domain.order.constant.OrderStatus;
import com.shop.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderItemHistResponseDto {

    private Long orderId; // 주문 아이디

    private String orderDate; // 주문 날짜

    private OrderStatus orderStatus; // 주문 상태

    private List<OrderItemResponseDto> orderItemResponseDto = new ArrayList<>(); // 주문 상품 리스트

    public OrderItemHistResponseDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderItemDto(OrderItemResponseDto orderItemResponseDto) {
        this.orderItemResponseDto.add(orderItemResponseDto);
    }
}
