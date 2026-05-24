package org.example.board_cafe_kiosk_2603.domain.admin.manager;

import lombok.*;

import java.time.LocalDateTime;

/*
 * 작성자 : 서주연
 * 기능 : Manager 클래스
 * 날짜 : 2026-03-30
 */

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
    private int id;
    private String loginId;
    private String password;
    private String name;
    private String email;  // OTP 발송 대상
    private RoleType role;  // Enum 타입 사용 (ADMIN, STAFF, SUPER)
    private boolean isActive;
    private LocalDateTime createdAt;
}
