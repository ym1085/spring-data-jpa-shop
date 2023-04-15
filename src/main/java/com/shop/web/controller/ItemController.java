package com.shop.web.controller;

import com.shop.service.ItemService;
import com.shop.web.controller.dto.ItemFormRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        log.info("Enter itemForm, /admin/item/new");
        model.addAttribute("itemFormRequestDto", new ItemFormRequestDto());
        return "/item/itemForm";
    }

    /**
     * 관리자 상품 등록
     * @param itemFormRequestDto : 상품명, 재고, 수량 등등..
     * @param itemImgFileList : 상품 파일 이미지
     */
    @PostMapping(value = "/admin/item/new")
    public String itemForm(
            @Valid @ModelAttribute ItemFormRequestDto itemFormRequestDto,
            BindingResult bindingResult,
            Model model,
            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        log.info("register products, itemFormRequestDto = {}", itemFormRequestDto.toString());
        log.info("register products, itemImgFileList.size = {}", itemImgFileList.size());

        /* DTO 파라미터 검증 */
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        /* 상품 이미지 검증 */
        if (itemImgFileList.get(0).isEmpty() && itemFormRequestDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormRequestDto, itemImgFileList);
        } catch (Exception e) {
            log.error("An error occurred during the product image registration process. e = {}", e.getMessage());
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/"; // 메인 화면 이동
    }

    /**
     * 상품 id 기반 상품 및 상품 이미지 조회
     * @param itemId : 상품 id
     */
    @GetMapping(value = "/admin/item/{itemId}")
    public String getItemAndItemImgByItemId(@PathVariable(name = "itemId") Long itemId,
                                            Model model) {
        log.info("retrive product by itemId, itemId = {}", itemId);
        ItemFormRequestDto itemFormRequestDto = new ItemFormRequestDto();
        String errorMessage = "";
        if (StringUtils.isBlank(String.valueOf(itemId))) { // Validator 사용하는 방법도 있음
            errorMessage = "유효하지 않은 상품 id입니다, 다시 시도해주세요.";
        } else if (!StringUtils.isNumeric(String.valueOf(itemId))) {
            errorMessage = "상품 id가 숫자가 아닙니다. 다시 시도해주세요.";
        } else {
            try {
                itemFormRequestDto = itemService.getItemAndItemImgByItemId(itemId);
            } catch (EntityNotFoundException e) {
                log.error("An exception was thrown because a single product was not found. e = {}", e.getMessage());
                errorMessage = "존재하지 않는 상품입니다.";
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("itemFormRequestDto", itemFormRequestDto);
        return "item/itemForm";
    }
}
