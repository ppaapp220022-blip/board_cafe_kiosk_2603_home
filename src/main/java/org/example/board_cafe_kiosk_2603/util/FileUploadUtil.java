package org.example.board_cafe_kiosk_2603.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
/*
 * 작성자 : 서주연
 * 기능 : 이미지 파일 업로드 유틸리티
 * 날짜 : 2026-03-30
 */

@Log4j2
@Component
public class FileUploadUtil {

    @Value("${my.upload.path}")  // MAC 설정 되어있음
    private String uploadPath;  // 파일의 저장 경로
    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            log.debug("업로드 파일 없음 - null 반환");
            return null;
        }

        // 저장 디렉토리 생성
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
            log.debug("업로드 디렉토리 생성: {}", uploadPath);
        }

        // UUID로 고유 파일명 생성
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString() + ext;

        // 파일 저장
        File savedFile = new File(uploadPath, savedName);
        file.transferTo(savedFile);
        log.debug("파일 저장 완료: {}", savedFile.getAbsolutePath());

        // DB에 저장할 접근 URL 반환
        return "/upload/" + savedName;
    }
    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return;

        String fileName = imageUrl.replace("/upload/", "");
        File file = new File(uploadPath, fileName);
        if (file.exists()) {
            file.delete();
            log.debug("파일 삭제 완료: {}", file.getAbsolutePath());
        }
    }
}
