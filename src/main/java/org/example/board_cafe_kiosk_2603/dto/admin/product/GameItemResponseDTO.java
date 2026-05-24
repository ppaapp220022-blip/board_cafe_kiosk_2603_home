package org.example.board_cafe_kiosk_2603.dto.admin.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItemStatus;

/*
 * 작성자 : 서주연
 * 기능 : 게임 아이템(재고) 조회 응답 시 사용하는 DTO
 * 날짜 : 2026-03-27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameItemResponseDTO {
    private int id;

    /* FK → game.id */
    private int gameId;

    /* JOIN으로 가져온 게임명 */
    private String gameName;

    /* 실물 시리얼 번호 */
    private String serialNumber;

    /* NORMAL / RENTED / DAMAGED / LOST => Enum */
    private GameItemStatus status;
}
