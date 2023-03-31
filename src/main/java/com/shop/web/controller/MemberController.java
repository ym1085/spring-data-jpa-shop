package com.shop.web.controller;

import com.shop.domain.member.entity.Member;
import com.shop.service.MemberService;
import com.shop.web.controller.dto.MemberFormRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입 화면 출력
     */
    @GetMapping(value = "/join")
    public String join(Model model) {
        model.addAttribute("MemberFormRequestDto", new MemberFormRequestDto());
        return "member/memberForm";
    }

    /**
     * 회원 가입 진행
     * @param memberFormRequestDto : 회원 가입을 원하는 회원의 회원 가입 정보
     */
    @PostMapping(value = "/join")
    public String join(@ModelAttribute("MemberFormRequestDto") @Valid MemberFormRequestDto memberFormRequestDto,
                       BindingResult bindingResult,
                       Model model) {

        // Neither BindingResult nor plain target object for bean name 'MemberFormRequestDto' available as request attribute
        // https://to-dy.tistory.com/31
        if (bindingResult.hasErrors()) {
            log.error("MemberFormRequestDto parameter is not valid..!");
            return "member/memberForm";
        }

        try {
            memberService.join(Member.createMember(memberFormRequestDto, passwordEncoder));
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

}
