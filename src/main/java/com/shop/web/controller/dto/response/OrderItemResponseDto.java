package com.shop.web.controller.dto.response;

import com.shop.domain.orderitem.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 이력 조회를 위한 Dto
 */
@Getter
@Setter
public class OrderItemResponseDto {
    private String itemName;
    private int count;
    private int orderPrice;
    private String imgUrl;

    public OrderItemResponseDto(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
