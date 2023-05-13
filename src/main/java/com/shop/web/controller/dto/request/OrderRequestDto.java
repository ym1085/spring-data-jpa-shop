package com.shop.web.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 상품 상세 페이지에서 주문할 상품의 아이디와 주문 수량 정보를 받는 Dto
 */
@Getter
@Setter
public class OrderRequestDto {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId; // 상품 아이디

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
    private int count; // 주문 수량
}
