package com.shop.web.controller.dto.request;

import com.shop.domain.itemimg.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgRequestDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    /*
        ItemImg 엔티티 객체를 파라미터로 받아 ItemImg 객체의 자료형과 맴버변수의 이름이 같을 때
        ItemImgRequestDto로 값을 복사해서 반환, static method로 선언하여 ItemImgRequestDto를 생성하지 않아도
        호출이 가능하다.
    */

    // ItemImg -> ItemImgRequestDto
    public static ItemImgRequestDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgRequestDto.class);
    }
}
