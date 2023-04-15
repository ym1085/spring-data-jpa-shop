package com.shop.service;

import com.shop.domain.itemimg.entity.ItemImg;
import com.shop.domain.itemimg.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (StringUtils.isNotBlank(originalFilename)) {
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일을 파일의 바이트 배열을 파일 업로드
            // 파라미터로 uploadFile 메서드를 호출한다. 호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장.
            imgName = fileService.uploadFile(itemImgLocation, originalFilename, file.getBytes());

            // 저장한 상품 이미지를 불러올 경로를 설정. 외부 리소스를 불러오는 urlPatterns/로 WebMvcConfig 클래스에서
            // /images/**를 설정해 주었음. 또한 application.yaml에서 설정한 uploadPath 프로퍼티 경로인 "C:/shop/ 아래
            // item 폴더에 이미지를 저장하므로 상품 이미지를 불러오는 경로로 "/images/item/"를 붙힌다
            imgUrl = "/images/item" + imgName;
        }
        itemImg.updateItemImg(originalFilename, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
}
