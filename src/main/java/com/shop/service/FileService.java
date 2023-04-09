package com.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

// @Log   : 로깅 프레임워크 없이도 로깅 기능을 제공하는 "Lombok"의 어노테이션
// @Slf4j : 로깅 프레임워크를 사용해야 하는 "SLF4J"의 어노테이션
//@Log
@Slf4j
@Service
public class FileService {

    /**
     * 파일 업로드
     * @param uploadPath : 파일 업로드 경로
     * @param originalFileName : 원본 파일명
     * @param fileData : 파일 바이너리 데이터
     * @return savedFileName : 업로드 된 파일명 반환
     * @throws Exception
     */
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // 파일명 중복을 피하기 위해서, 랜덤 난수 생성

        // 파일 확장자 추출
        String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출

        // 난수 + 확장자를 기반으로 파일명 생성
        String savedFileName = uuid.toString() + fileExt;

        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        log.info("fileUploadFullUrl = {}", fileUploadFullUrl);
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 바이트 단위의 출력을 내보내는 클래스, 파일 출력 스트림을 만든다
        fos.write(fileData); // 파일을 파일 출력 스트림에 입력
        fos.close();
        return savedFileName; // 업로드 된 파일명 반환
    }

    /**
     * 파일 삭제
     * @param filePath
     * @throws Exception
     */
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath); // 파일이 저장된 경로를 통해 파일 객체 생성
        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.warn("파일이 존재하지 않습니다.");
        }
    }
}
