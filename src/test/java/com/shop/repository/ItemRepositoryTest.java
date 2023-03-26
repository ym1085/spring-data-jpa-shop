package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional // https://okky.kr/questions/873387
@SpringBootTest
class ItemRepositoryTest {

    private static final int CREATE_ITEM_LOOP_COUNT = 10;

    // @persistenceContext annotation is an annotation used to inject the Entity Manager into the bean from the spring boot
    // Spring Boot use singleton bean architecture, so all thread share spring bean object..
    // Look up on the issue of concurrency
    // https://batory.tistory.com/497
    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    @Order(1)
    void createSingleItemTest() throws Exception {
        //given
        Item item = Item.builder()
                .itemName("테스트 상품 1")
                .price(10000)
                .itemDetail("테스트 상품 상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .regDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        //when
        Item result = itemRepository.save(item);
//        System.out.println("result => " + result.toString());

        //then
        assertThat(result).isNotNull();
        assertThat(result.getItemName()).isEqualTo("테스트 상품 1");
//        assertThat(result.getPrice()).isEqualTo(1000);
        assertThat(result.getPrice()).isEqualTo(10000);
        assertThat(result.getItemDetail()).isEqualTo("테스트 상품 상세설명");
    }

    @Test
    @DisplayName("상품명 기반 상품 조회 테스트")
    @Order(2)
    void findItemByItemNameTest() throws Exception {
        //given
        this.createSampleItemList();

        //
        List<Item> result = itemRepository.findByItemName("테스트 상품1");
//        System.out.println("result => " + result.toString());

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getItemName()).isEqualTo("테스트 상품1");
        assertThat(result.get(0).getPrice()).isEqualTo(10001);
        assertThat(result.get(0).getItemDetail()).isEqualTo("테스트 상품 상세설명1");
    }

    private void createSampleItemList() {
        for (int i = 1; i < CREATE_ITEM_LOOP_COUNT + 1; i++) {
            Item item = Item.builder()
                    .itemName("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세설명" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .regDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 OR 테스트")
    @Order(3)
    void findItemByItemNameOrItemDetailTest() throws Exception {
        //given
        this.createSampleItemList();

        //when
        List<Item> result = itemRepository.findByItemNameOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
//        System.out.println("result => " + result.toString());

        //then
        assertThat(result).isNotNull();
        assertThat(result.get(0).getItemName()).isEqualTo("테스트 상품1");
        assertThat(result.get(0).getItemDetail()).isEqualTo("테스트 상품 상세설명1");
    }

    @Test
    @DisplayName("특정 상품 가격 미만(LessThan)인 상품 조회")
    @Order(4)
    void findItemByPriceLessThanTest() throws Exception {
        //given
        this.createSampleItemList();

        //when
        List<Item> result = itemRepository.findByPriceLessThan(10005);// 10000, 10001, 10002, 10003, 10004
        System.out.println("result.get(result.size() - 1).getPrice() => " + result.get(result.size() - 1).getPrice());

        //then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.size()).isEqualTo(5);
        assertTrue(result.get(result.size() - 1).getPrice() < 10005, "Last result data less than 10005");
    }

    @Test
    @DisplayName("특정 상품 가격 미만(LessThan)인 상품 조회, 내림차순 정렬")
    @Order(5)
    void findItemByPriceLessThanOrderByPriceDescTest() throws Exception {
        //given
        this.createSampleItemList();

        //when
        List<Item> result = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

        //then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.size()).isEqualTo(5);
        assertTrue(result.get(result.size() - 1).getPrice() < 10005, "Last result data less than 10005");
        assertThat(result.get(0).getPrice()).isEqualTo(10004);
        assertThat(result.get(1).getPrice()).isEqualTo(10003);
        assertThat(result.get(2).getPrice()).isEqualTo(10002);
        assertThat(result.get(3).getPrice()).isEqualTo(10001);
        assertThat(result.get(4).getPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("@Query를 이용한 상세 상품 조회 테스트")
    @Order(6)
    void findByItemDetailTest() throws Exception {
        //given
        this.createSampleItemList();

        //when
        List<Item> result = itemRepository.findByItemDetail("테스트 상품 상세설명");
        System.out.println("result => " + result.toString());

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("@Query와 native query 옵션을 사용하여 상세 상품 조회 테스트")
    @Order(7)
    void findByItemDetailNativeTest() throws Exception {
        //given
        this.createSampleItemList();

        //when
        List<Item> result = itemRepository.findByItemDetailNative("테스트 상품 상세설명");

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("QueryDSL 조회 기능 테스트")
//    @Rollback(value = false)
    void queryDslTest() throws Exception {
        //given
        this.createSampleItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.em); // entityManager to JPAQueryFactory
        QItem qItem = QItem.item;

        //when
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> result = query.fetch();
//        System.out.println("result => " + result.toString());
//        System.out.println("result.get(result.size() - 1).getItemDetail() => " + result.get(result.size() - 1).getItemDetail());

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.get(0).getItemDetail()).isEqualTo("테스트 상품 상세설명10"); // 가격 기준 내림차순이기 때문에 10번이 나와야함
    }

    private void createSampleItemListForQueryDsl() {
        for (int i = 1; i <= 5; i++) {
            Item item = Item.builder()
                    .itemName("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세설명" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .regDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = Item.builder()
                    .itemName("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세설명" + i)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .stockNumber(0)
                    .regDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트, Predicate 사용")
    void queryDslTestWithPredicate() throws Exception {
        //given
        this.createSampleItemListForQueryDsl();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qItem = QItem.item;

        String itemDetail = "테스트 상품 상세설명";
        int price = 10003; // 10000 ~ 10005까지 dummy 데이터 생성
        String itemSellStat = "SELL";

        booleanBuilder.and(qItem.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(qItem.price.gt(price));
        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5); // page : 0,  size : 5

        //when
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("itemPagingResult: " + itemPagingResult);
        System.out.println("itemPagingResult.getTotalElements() => " + itemPagingResult.getTotalElements()); // conting
        System.out.println("itemPagingResult.getContent() => " + itemPagingResult.getContent()); // contents

        //then
        assertThat(itemPagingResult).isNotNull();
        assertThat(itemPagingResult.getTotalElements()).isEqualTo(2);
    }
}
