package com.shop.web.controller;

import com.shop.service.OrderService;
import com.shop.web.controller.dto.request.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;

    /**
     * 상품 주문
     *
     * RequestBody : HTTP 요청의 본문 Body에 담긴 내용을 자바 객체로 전달
     * ResponseBody : 자바 객체를 HTTP 요청의 Body로 전달
     * Principal : 현재 로그인 한 유저의 정보를 얻기 위해 principal 객체 사용
     *
     * @param orderRequestDto 상품 ID, 상품 수량
     */
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                              BindingResult bindingResult,
                                              Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;
        try {
            orderId = orderService.order(orderRequestDto, email);
        } catch (Exception e) {
            log.error("상품 등록 과정에서 오류가 발생하였습니다. e = {}", e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
