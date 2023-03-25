package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yaml")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
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
}
