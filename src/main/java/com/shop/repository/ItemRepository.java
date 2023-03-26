package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /* 상품명을 이용하여 상품 조회 */
    List<Item> findItemByItemName(String itemName);

    /* 상품명과 상세 상품명을 이용하여 상품 조회 */
    List<Item> findItemByItemNameOrItemDetail(String itemName, String itemDetail);

    /* 상품 가격보다 작업 상품 조회 */
    List<Item> findItemByPriceLessThan(Integer price);

    /* 상품 가격 내림차순으로 정렬 */
    List<Item> findItemByPriceLessThanOrderByPriceDesc(Integer price);

}
