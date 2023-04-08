package com.shop.web.controller;

import com.shop.web.controller.dto.ItemFormRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ItemController {

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        log.info("Enter itemForm, /admin/item/new");
        model.addAttribute("itemFormRequestDto", new ItemFormRequestDto());
        return "/item/itemForm";
    }
}
