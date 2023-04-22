package com.shop.web.controller;

import com.shop.service.ItemService;
import com.shop.web.controller.dto.request.ItemSearchRequestDto;
import com.shop.web.controller.dto.response.MainItemResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final ItemService itemService;

    /**
     * 메인 페이지 이동
     */
    @GetMapping(value = "/")
    public String main(ItemSearchRequestDto itemSearchRequestDto,
                       Optional<Integer> page,
                       Model model) {

        PageRequest pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemResponseDto> items = itemService.getMainItemPage(itemSearchRequestDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchRequestDto", itemSearchRequestDto);
        model.addAttribute("maxPage", 5); // 화면에 보여줄 페이지 갯수
        return "main";
    }
}
