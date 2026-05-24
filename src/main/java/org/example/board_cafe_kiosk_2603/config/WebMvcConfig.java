package org.example.board_cafe_kiosk_2603.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * 작성자 : 서주연
 * 기능 : 업로드 파일을 정적 리소스로 서빙하기 위한 WebMvc 설정
 * 날짜 : 2026-03-30
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${my.upload.path}")
    private String uploadPath;  // 파일의 저장 경로

    /*
     * 작성자 : 서주연
     * 기능 : addResourceHandlers 메서드
     * 날짜 : 2026-03-30
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadPath + "/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
    }
}
