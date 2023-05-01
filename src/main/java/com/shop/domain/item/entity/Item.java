package com.shop.domain.item.entity;

import com.shop.domain.BaseEntity;
import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.exception.OutOfStockException;
import com.shop.web.controller.dto.request.ItemFormRequestDto;
import lombok.*;

import javax.persistence.*;

// https://yuja-kong.tistory.com/entry/Spring-ModelMapper-Entity-to-DTO-%EB%B3%80%ED%99%98-%EC%8B%9C-%ED%94%84%EB%A1%9C%ED%8D%BC%ED%8B%B0-null-%ED%95%B4%EA%B2%B0
// ItemServiceTest에서 상품, 이미지 등록 시 ModelMapper 사용하는 경우 null값으로 초기화 되는 이슈 발생하여
// DTO에는 기존에 있어서, 엔티티에 @Setter 어노테이션 추가 해주었음, @Setter를 기반으로 ModelMapper가 값을 셋팅 하는 듯

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
    public Item(Long id, String itemName, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
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

    /*
    유저가 상품 주문 시 기존 Item 엔티티가 가지고 있는 StockNumber(재고)를 차감하는 메서드
    만약 요청한 상품 갯수가 재고보다 많을 경우에는 Exception 발생
    */
    public void removeItemStock(int stockNumber) {
        int restStockNum = this.stockNumber - stockNumber;
        if (restStockNum < 0) { // 재고 0 이하
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStockNum; // 현재 stockNumber - 주문 시 받은 재고 => 엔티티에 업데이트
    }
}
