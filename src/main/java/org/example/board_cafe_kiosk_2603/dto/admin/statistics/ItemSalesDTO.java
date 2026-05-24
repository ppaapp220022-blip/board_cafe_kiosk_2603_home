package org.example.board_cafe_kiosk_2603.dto.admin.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 작성자 : 강수연
 * 기능 : ItemSales 데이터 전달 객체
 * 날짜 : 2026-04-02
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSalesDTO {
    private String menuName;    // 메뉴명 (JOIN 필요)
    private String category;    // 카테고리 (COFFEE, FOOD 등)
    private Integer salesQty;   // 판매 수량
    private Long salesAmount;   // 판매 금액
}
