package org.example.board_cafe_kiosk_2603.dto.admin.manager;

import lombok.Getter;

/*
 * 작성자 : 서주연
 * 기능 : ProfileUpdateRequest 클래스
 * 날짜 : 2026-04-08
 */

@Getter
public class ProfileUpdateRequest {
    private String name;
    private String password; // 빈 값이면 변경 안 함
    private String otp;      // 추가
}
