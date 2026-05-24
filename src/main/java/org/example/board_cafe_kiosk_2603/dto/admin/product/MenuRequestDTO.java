package org.example.board_cafe_kiosk_2603.dto.admin.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 작성자 : 서주연
 * 기능 : 메뉴 등록·수정 요청 시 사용하는 DTO
 * 날짜 : 2026-03-27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDTO {

    /* FK → category.id */
    private Integer categoryId;

    private String name;
    private int price;
    private String description;
    private String imageUrl;

    /* 판매 가능 여부 */
    private boolean isAvailable;
}
