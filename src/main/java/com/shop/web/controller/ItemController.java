package com.shop.web.controller;

import com.shop.domain.item.entity.Item;
import com.shop.service.ItemService;
import com.shop.web.controller.dto.request.ItemFormRequestDto;
import com.shop.web.controller.dto.request.ItemSearchRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
     *
     * @param itemFormRequestDto : 상품명, 재고, 수량 등등..
     * @param itemImgFileList    : 상품 파일 이미지
     */
    @PostMapping(value = "/admin/item/new")
    public String itemForm(
            @Valid ItemFormRequestDto itemFormRequestDto,
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
     *
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

    /**
     * 상품 id 기반 상품 및 상품 이미지 수정
     *
     * @param itemFormRequestDto : 상품 데이터(상품명, 가격, 상품 상세, 재고)
     * @param itemImgFileList    : 상품 이미지
     */
    @PostMapping(value = "/admin/item/{itemId}")
    public String updateItem(@Valid ItemFormRequestDto itemFormRequestDto,
                             BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "/item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormRequestDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 값 입니다.");
            return "/item/itemForm";
        }

        try {
            itemService.updateItem(itemFormRequestDto, itemImgFileList);
        } catch (Exception e) {
            log.error("Updating a product failed, e = {}", e.getMessage());
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    /**
     * 상품 관리 -> 상품 조회 및 검색 시 사용
     * 조건
     * - 기간(1d, 1w, 1m, 6m)
     * - 판매상태(판매중, 품절)
     * - 상품명, 상품 등록자 Id
     * - 검색어
     */
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) // 페이지 번호 있는 경우, 없는 경우 URL 처리
    public String itemMange(ItemSearchRequestDto itemSearchRequestDto,
                            @PathVariable("page") Optional<Integer> page,
                            Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); // 0: 조회할 페이지 번호, 3: 한 번에 가지고 올 데이터 수
        Page<Item> items = itemService.getAdminItemPage(itemSearchRequestDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchRequestDto", itemSearchRequestDto); // 페이지 전환 시 기존 검색 조건 유지한 채 이동할 수 있도록 다시 전달
        model.addAttribute("maxPage", 5); // 상품 관리 메뉴 하단에 보여줄 페이지 번호 최대 개수
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        log.info("itemDetail, itemId = {}", itemId);
        model.addAttribute("itemId", itemId);
        model.addAttribute("item", itemService.getItemAndItemImgByItemId(itemId));
        return "item/itemDetail";
    }
}
