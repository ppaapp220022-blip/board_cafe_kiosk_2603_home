package org.example.board_cafe_kiosk_2603.dto.admin.macro;

import lombok.Data;

/*
 * 작성자 : 강수연
 * 기능 : MacroSendRequest 데이터 전달 객체
 * 날짜 : 2026-04-08
 */

@Data
public class MacroSendRequestDTO {
    private Long id;
    private Integer tableId;
    private Integer macroMessageId;
}
