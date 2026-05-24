package org.example.board_cafe_kiosk_2603.dto.admin.macro;

import lombok.*;
import org.example.board_cafe_kiosk_2603.domain.admin.macro.MacroMessage;

/*
 * 작성자 : 강수연
 * 기능 : MacroMessageResponse 데이터 전달 객체
 * 날짜 : 2026-03-25
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MacroMessageResponseDTO {
    private Integer id;
    private String direction;
    private String messageText;
    /*
     * 작성자 : 강수연
     * 기능 : Entity를 DTO로 변환하는 정적 팩토리 메서드
     * 날짜 : 2026-03-25
     */

    public static MacroMessageResponseDTO from(MacroMessage entity) {
        return MacroMessageResponseDTO.builder()
                .id(entity.getId())
                .direction(entity.getDirection())
                .messageText(entity.getMessageText())
                .build();
    }
}
