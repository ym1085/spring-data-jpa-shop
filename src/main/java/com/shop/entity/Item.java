package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Getter
//@Setter // Entity에 setter 사용은 지양, 추후 빼야함
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item {

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

    private LocalDateTime regDate; // 상품 등록일

    private LocalDateTime updateDate; // 상품 수정일

    @Builder
    public Item(Long id, String itemName, int price, int stockNumber, String itemDetail,
                ItemSellStatus itemSellStatus, LocalDateTime regDate, LocalDateTime updateDate) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.regDate = regDate;
        this.updateDate = updateDate;
    }
}
