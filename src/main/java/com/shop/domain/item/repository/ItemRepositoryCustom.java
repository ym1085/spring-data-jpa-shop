package com.shop.domain.item.repository;

import com.shop.domain.item.entity.Item;
import com.shop.web.controller.dto.request.ItemSearchRequestDto;
import com.shop.web.controller.dto.response.MainItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchRequestDto itemSearchRequestDto, Pageable pageable);

    Page<MainItemResponseDto> getMainItemPage(ItemSearchRequestDto itemSearchRequestDto, Pageable pageable);

}
