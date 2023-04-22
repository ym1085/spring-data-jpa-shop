package com.shop.web.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemTestRequestDto {
    private Long id;
    private String itemName;
    private Integer price;
    private String itemDetail;
    private String sellStatCd;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    // Use @Builder Pattern
}
