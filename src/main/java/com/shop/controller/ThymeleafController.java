package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/thymeleaf")
@Controller
public class ThymeleafController {

    @GetMapping(value = "/main")
    public String thymeleafMain(Model model) {
        model.addAttribute("data", "타임리프 테스트");
        return "thymeleafEx/thymeleafEx01";
    }
}
