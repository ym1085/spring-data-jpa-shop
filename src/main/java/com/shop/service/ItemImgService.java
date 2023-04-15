package com.shop.service;

import com.shop.domain.itemimg.entity.ItemImg;
import com.shop.domain.itemimg.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    /**
     * 상품 등록 후 상품 이미지 저장
     * @param itemImg : 이미지 데이터 저장 객체
     * @param itemImgFile : 업로드 파일
     * @throws Exception
     */
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String originalFilename = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (StringUtils.isNotBlank(originalFilename)) {
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일을 파일의 바이트 배열을 파일 업로드
            // 파라미터로 uploadFile 메서드를 호출한다. 호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장.
            imgName = fileService.uploadFile(itemImgLocation, originalFilename, itemImgFile.getBytes());

            // 저장한 상품 이미지를 불러올 경로를 설정. 외부 리소스를 불러오는 urlPatterns/로 WebMvcConfig 클래스에서
            // /images/**를 설정해 주었음. 또한 application.yaml에서 설정한 uploadPath 프로퍼티 경로인 "C:/shop/ 아래
            // item 폴더에 이미지를 저장하므로 상품 이미지를 불러오는 경로로 "/images/item/"를 붙힌다
            imgUrl = "/images/item" + imgName;
        }
        itemImg.updateItemImg(originalFilename, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    /**
     * 상품 상세 화면에서 상품 수정 시 이미지 업데이트 진행
     * @param itemId
     * @param itemImgFile
     * @throws Exception
     */
    public void updateItemImg(Long itemId, MultipartFile itemImgFile) throws Exception { // 하나의 Transaction 내에서의 Update
        if (!itemImgFile.isEmpty()) { // 상품 이미지 수정한 경우
            ItemImg savedItemImg = itemImgRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException());
            if (StringUtils.isNotBlank(savedItemImg.getOriImgName())) { // banana.png, banana.jpeg.. ---> 기존 이미지 파일 삭제
                // FIXME: 삭제 되는지 확인 필요 -> uuid + ext 로 파일명 되어있는데 어떻게 삭제하라는겨?
                // -> savedItemImg.getOriImgName() -> UUID + ext 니까 이걸로 삭제 합시다
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getOriImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            // savedItemImg은 현재 Transaction 안에서 영속(Persists) 상태이기에, 별도의 save 처리를 하지 않아도 update 진행 됨
            // 영속성 컨텍스트 안에는 1차 캐시가 존재, 맨 처음에 영속화 할 때 최초로 1번 스냅샷 떠두고 그 스냅샷이랑 계속 비교
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
