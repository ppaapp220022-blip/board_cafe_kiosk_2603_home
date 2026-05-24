package org.example.board_cafe_kiosk_2603.dto.admin.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.board_cafe_kiosk_2603.domain.admin.product.CategoryType;

/*
 * 작성자 : 서주연
 * 기능 : 카테고리 등록, 수정 요청 DTO
 * 날짜 : 2026-03-27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDTO {
    private String name;
    private CategoryType type;  // Enum
}
