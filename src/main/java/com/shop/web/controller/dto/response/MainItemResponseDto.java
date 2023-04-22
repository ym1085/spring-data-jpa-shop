package com.shop.web.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemResponseDto {
    private Long id;
    private String itemName;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    /* QueryDSL 조회 시 MainItemResponseDto 객체로 바로 받아서 사용 -> QueryDSL 조회 시 Entity -> DTO로 변환 가능 */
    @QueryProjection
    public MainItemResponseDto(Long id, String itemName, String itemDetail, String imgUrl, Integer price) {
        this.id = id; // 상품 id
        this.itemName = itemName; // 상품명
        this.itemDetail = itemDetail; // 상품 상세 내용
        this.imgUrl = imgUrl; // 이미지 경로(대표 이미지의 경로)
        this.price = price; // 가격
    }
}
