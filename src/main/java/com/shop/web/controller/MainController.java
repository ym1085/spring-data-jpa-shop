package com.shop.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

    /**
     * 메인 페이지 이동
     */
    @GetMapping(value = "/")
    public String main() {
        return "main";
    }
}
