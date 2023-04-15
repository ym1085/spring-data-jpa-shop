package com.shop.service;

import com.shop.domain.item.entity.Item;
import com.shop.domain.item.repository.ItemRepository;
import com.shop.domain.itemimg.entity.ItemImg;
import com.shop.domain.itemimg.repository.ItemImgRepository;
import com.shop.web.controller.dto.ItemFormRequestDto;
import com.shop.web.controller.dto.ItemImgRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

// Transcation 종료 시점에 영속성 컨텍스트의 데이터를 DB에 밀어 넣는다

// @Transactional 어노테이션이 선언된 메서드 내에서 일어난 모든 데이터베이스 작업은
// 트랜잭션 범위에서 수행됩니다. 메서드가 호출되면, 트랜잭션을 시작하고, 메서드의 실행이 완료될 때까지
// 트랜잭션을 유지합니다. 그리고 메서드의 실행이 종료되면, 트랜잭션 범위 내에서 수행된 모든 데이터베이스
// 작업이 커밋됩니다. 따라서, @Transactional 어노테이션이 선언된 메서드에서 수행되는 모든 데이터베이스
// 작업은 트랜잭션 범위에서 수행되며, 트랜잭션 커밋도 해당 메서드의 실행이 완료된 후에 발생합니다.

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    /**
     * 상품 및 상품 이미지 등록
     */
    public Long saveItem(ItemFormRequestDto itemFormRequestDto, List<MultipartFile> files) throws Exception {
        Item item = itemFormRequestDto.createItem(); // Dto to Entity(Item) 변환 후 상품 등록 진행
        itemRepository.save(item);

        for (int i = 0; i < files.size(); i++) { // 이미지 등록
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0) {
                itemImg.setRepimgYn("Y"); // 대표 이미지 지정
            } else {
                itemImg.setRepimgYn("N"); // 대표 이미지 지정
            }
            itemImgService.saveItemImg(itemImg, files.get(i));
        }

        return item.getId();
    }

    /**
     * 상품 id 기반 상품 및 상품 이미지 조회
     */
    @Transactional(readOnly = true) // 변경감지(dirty checking)을 수행하지 않아, 성능상 이점이 있음
    public ItemFormRequestDto getItemAndItemImgByItemId(Long itemId) {
        // 상품 테이블(1) : 상품 이미지 테이블(N)
        List<ItemImg> findItemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 아이템 이미지 데이터 조회
        List<ItemImgRequestDto> itemImgRequestDtoList = new ArrayList<>();
        for (ItemImg findItemImg : findItemImgList) {
            itemImgRequestDtoList.add(ItemImgRequestDto.of(findItemImg));  // Entity: ItemImg  -> DTO: ItemImgRequestDto
        }

        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        ItemFormRequestDto itemFormRequestDto = ItemFormRequestDto.of(findItem);
        itemFormRequestDto.setItemImgRequestDtoList(itemImgRequestDtoList);

        log.info("itemFormRequestDto = {}", itemFormRequestDto);
        log.info("itemImgRequestDtoList = {}", itemImgRequestDtoList);
        return itemFormRequestDto;
    }
}