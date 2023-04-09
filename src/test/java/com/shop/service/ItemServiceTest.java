package com.shop.service;

import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.domain.item.entity.Item;
import com.shop.domain.item.repository.ItemRepository;
import com.shop.domain.itemimg.entity.ItemImg;
import com.shop.domain.itemimg.repository.ItemImgRepository;
import com.shop.web.controller.dto.ItemFormRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    public List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    void test() throws Exception {
        //given
        // 상품 데이터 생성
        ItemFormRequestDto itemFormRequestDto = new ItemFormRequestDto();
        itemFormRequestDto.setItemName("테스트 상품1");
        itemFormRequestDto.setItemSellStatus(ItemSellStatus.SELL); // 판매
        itemFormRequestDto.setItemDetail("테스트 상품1 입니다.");
        itemFormRequestDto.setPrice(10000);
        itemFormRequestDto.setStockNumber(100);

        //when
        // 이미지 생성
        List<MultipartFile> multipartFiles = this.createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormRequestDto, multipartFiles);

        // 해당 Item id에 매핑되는 이미지 정보 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        System.out.println(">>> itemImgList => " + itemImgList);

        // 해당 Item id에 매핑되는 상품 정보 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        System.out.println(">>> item => " + item);

        //then
        assertThat(itemFormRequestDto.getItemName()).isEqualTo(item.getItemName());
        assertThat(itemFormRequestDto.getItemSellStatus()).isEqualTo(item.getItemSellStatus());
        assertThat(itemFormRequestDto.getItemDetail()).isEqualTo(item.getItemDetail());
        assertThat(itemFormRequestDto.getPrice()).isEqualTo(item.getPrice());
        assertThat(itemFormRequestDto.getStockNumber()).isEqualTo(item.getStockNumber());
    }
}
