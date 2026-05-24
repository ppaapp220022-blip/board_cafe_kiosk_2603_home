package org.example.board_cafe_kiosk_2603.domain.admin.macro;

import lombok.*;

import java.time.LocalDateTime;

/*
 * 작성자 : 강수연
 * 기능 : AdminTableMessage 클래스
 * 날짜 : 2026-04-08
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminTableMessage {
    private Long id;
    private Integer tableId;
    private Integer macroId;
    private String direction;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
}
