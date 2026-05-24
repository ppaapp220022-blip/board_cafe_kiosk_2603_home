package org.example.board_cafe_kiosk_2603.dto.admin.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * 작성자 : 서주연
 * 기능 : 메뉴 조회 응답 시 사용하는 DTO
 * 날짜 : 2026-03-27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDTO {

    private int id;
    private Integer categoryId;

    /** JOIN으로 가져온 카테고리명 */
    private String categoryName;

    /** 카테고리 타입 (FOOD, DRINK, GAME 등) - 게임 판별 로직에 사용 */
    private String categoryType;

    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private boolean isAvailable;
    private boolean isDeleted;
    private LocalDateTime createdAt;
}
