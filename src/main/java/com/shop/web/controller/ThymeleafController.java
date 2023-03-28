package com.shop.web.controller;

import com.shop.web.controller.dto.ItemTestRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 2023-03-29
 * @author ymkim
 * @desc View Test Controller
 */
@Slf4j
@Controller
public class ThymeleafController {

    @GetMapping(value = "/item/main")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 테스트");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value = "/item/single")
    public String thymeleafExample02(Model model) {
        ItemTestRequestDto itemTestRequestDto = new ItemTestRequestDto();
        itemTestRequestDto.setItemDetail("상품 상세 설명");
        itemTestRequestDto.setItemName("테스트 상품 1");
        itemTestRequestDto.setPrice(10000);
        itemTestRequestDto.setRegDate(LocalDateTime.now());

        model.addAttribute("itemTestRequestDto", itemTestRequestDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/item/list")
    public String thymeleafExample03(Model model) {
        List<ItemTestRequestDto> itemTestRequestDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemTestRequestDto itemTestRequestDto = new ItemTestRequestDto();
            itemTestRequestDto.setItemDetail("상품 상세 설명" + i);
            itemTestRequestDto.setItemName("테스트 상품" + i);
            itemTestRequestDto.setPrice(1000 * i);
            itemTestRequestDto.setRegDate(LocalDateTime.now());
            itemTestRequestDtoList.add(itemTestRequestDto);
        }
        model.addAttribute("itemTestRequestDtoList", itemTestRequestDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/item/v2/list")
    public String thymeleafExample04(Model model) {
        List<ItemTestRequestDto> itemTestRequestDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemTestRequestDto itemTestRequestDto = new ItemTestRequestDto();
            itemTestRequestDto.setItemDetail("상품 상세 설명" + i);
            itemTestRequestDto.setItemName("테스트 상품" + i);
            itemTestRequestDto.setPrice(1000 * i);
            itemTestRequestDto.setRegDate(LocalDateTime.now());
            itemTestRequestDtoList.add(itemTestRequestDto);
        }
        model.addAttribute("itemTestRequestDtoList", itemTestRequestDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/item/link")
    public String thymeleafExample05() {
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/item/param")
    public String thymeleafExample06(@RequestParam("param1") String param1,
                                     @RequestParam("param2") String param2,
                                     Model model) {
        // Front 영역에서 파라미터 받아서 바로 해당 파라미터 넘기는 행위는 지양합시다... 예제일뿐 :)
        log.info("param1 = {}, param2 = {}", param1, param2);
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }
}
