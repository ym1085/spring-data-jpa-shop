package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    /* 상품명을 이용하여 상품 조회 */
    List<Item> findByItemName(String itemName);

    /* 상품명과 상세 상품명을 이용하여 상품 조회 */
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    /* 상품 가격보다 작업 상품 조회 */
    List<Item> findByPriceLessThan(Integer price);

    /* 상품 가격 내림차순으로 정렬 */
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    /* @Query 어노테이션 기반 상세 상품명을 이용하여 상품 조회 */
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc") // 파라미터 바인딩은 이름 기반
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    /* @Query native query 옵션 기반 상세 상품명을 이용하여 상품 조회 */
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);
}
