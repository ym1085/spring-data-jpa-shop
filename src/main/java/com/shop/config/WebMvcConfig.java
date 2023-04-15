package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    private String uploadPath; // application.yml에 지정한 파일 업로드 경로

    /**
     * addResourceHandler("/url")
     * - web browser에 입력하는 url에 /images로 시작하는 경우 uploadPath에 설정한 폴더 를 기준으로
     *   파일을 읽어오도록 설정
     *
     * addResourceLocations(path)
     * - 로컬 컴퓨터에 저장된 파일을 읽어올 root 경로 지정
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본적으로 src/main/resources 밑에 사용자의 파일을 업로드하는 것은 보안상 좋지가 않음
        // 그렇기에 외부 경로에 파일을 저장하고 addResourceLocations를 통해 해당 외부 경로를 지정한다
        // 이렇게 하면 localhost:8080/images/** URL을 치고 들어오면, uploadPath 경로로 포워딩을 하게 됨

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath); // Window : file:///C:/shop/, Linux : file:/DATA/video/ 지정

        /*registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/", "classpath:/static/");*/
    }
}
