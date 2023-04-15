package com.shop.domain.item.entity;

import com.shop.domain.BaseEntity;
import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.web.controller.dto.ItemFormRequestDto;
import lombok.*;

import javax.persistence.*;

// https://yuja-kong.tistory.com/entry/Spring-ModelMapper-Entity-to-DTO-%EB%B3%80%ED%99%98-%EC%8B%9C-%ED%94%84%EB%A1%9C%ED%8D%BC%ED%8B%B0-null-%ED%95%B4%EA%B2%B0
// FIXME: ItemServiceTest에서 상품, 이미지 등록 시 ModelMapper 사용하는 경우 null값으로 초기화 되는 이슈 발생하여
// DTO에는 기존에 있어서, 엔티티에 @Setter 어노테이션 추가 해주었음, @Setter를 기반으로 ModelMapper가 값을 셋팅 하는 듯

@ToString
@Getter
@Setter // Entity에 setter 사용은 지양, 추후 빼야함
@Table(name = "item")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Entity
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드(PK)

    @Column(nullable = false, length = 50)
    private String itemName; // 상품명

    @Column(name = "price", nullable = false)
    private int price; // 상품 가격

    @Column(nullable = false)
    private int stockNumber; // 상품 재고

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    /*private LocalDateTime regDate; // 상품 등록일

    private LocalDateTime updateDate; // 상품 수정일*/

    @Builder
    public Item(Long id, String itemName, int price, int stockNumber, String itemDetail,
                ItemSellStatus itemSellStatus) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }

    /* 상품 상세 화면 -> 상품 업데이트(Dirty Checking // 변경 감지)용 */
    public void updateItem(ItemFormRequestDto itemFormRequestDto) {
        this.itemName = itemFormRequestDto.getItemName();
        this.price = itemFormRequestDto.getPrice();
        this.stockNumber = itemFormRequestDto.getStockNumber();
        this.itemDetail = itemFormRequestDto.getItemDetail();
        this.itemSellStatus = itemFormRequestDto.getItemSellStatus(); // enum
    }
}
