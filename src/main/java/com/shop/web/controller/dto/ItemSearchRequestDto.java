package com.shop.web.controller.dto;

import com.shop.domain.item.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchRequestDto {
    private String searchDateType; // all, 1d, 1w, 1m, 6m
    private ItemSellStatus searchSellStatus; // 판매중, 품절
    private String searchBy; // 상품명: itemName // 등록자 ID: createdBy
    private String searchQuery; // 검색어
}
