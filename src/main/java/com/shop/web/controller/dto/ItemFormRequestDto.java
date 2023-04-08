package com.shop.web.controller.dto;

import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.domain.item.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 등록은 어차피 관리자가 수행하는 행위
 */
@Getter
@Setter
public class ItemFormRequestDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName;

    @NotBlank(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotBlank(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    /* 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트 */
    private List<ItemImgRequestDto> itemImgRequestDtoList = new ArrayList<>();

    /*
        상품의 아이디를 저장하는 리스트. 상품 등록 시에는 아직 상품의 이미지를 저장하지 않았기 때문에
        아무 값도 들어가 있지 않고 수정 시에 이미지 아이디를 담아둘 용도로 사용
    */
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    /* ItemFormRequestDto -> Item */
    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    /* Item -> ItemFormRequestDto */
    public static ItemFormRequestDto of(Item item) {
        return modelMapper.map(item, ItemFormRequestDto.class);
    }
}
