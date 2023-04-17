package com.shop.domain.item.repository;

import com.shop.domain.item.entity.Item;
import com.shop.web.controller.dto.ItemSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchRequestDto itemSearchRequestDto, Pageable pageable);

}
