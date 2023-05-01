package com.shop.exception;

/**
 * 상품 등록시 현재 존재하는 재고보다 큰 주문 갯수가 들어오는 경우 Exception 발생
 */
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }
}
