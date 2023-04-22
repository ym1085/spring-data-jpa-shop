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

        // 'n' 페이지에 6개의 데이터(size) 요청 -> 아마 이런식으로 들어올 듯, PageRequest.of(페이지 번호, 보여줄 데이터 갯수)
        // FIXME: page에 맞게 페이징 처리를 해야 하는데 현재 페이징 이상 없음 수정 필요
        PageRequest pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemResponseDto> items = itemService.getMainItemPage(itemSearchRequestDto, pageable);
        //int number = items.getNumber(); // 현재 페이지 수 -> 페이지는 0부터 시작한다 -> 0, 1, 2, 3...
        //int totalPages = items.getTotalPages();// 전체 페이지 수
        model.addAttribute("items", items);
        model.addAttribute("itemSearchRequestDto", itemSearchRequestDto);
        model.addAttribute("maxPage", 5); // 화면에 보여줄 페이지 갯수
        return "main";
    }
}
