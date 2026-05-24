package org.example.board_cafe_kiosk_2603.dto.admin.statistics;

import lombok.*;

/*
 * 작성자 : 강수연
 * 기능 : DailySales 데이터 전달 객체
 * 날짜 : 2026-04-01
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailySalesDTO {
    private String statDate;    // X축: 날짜 (2026-03-31)
    private Long totalRevenue;  // Y축: 매출액
    private Integer orderCount; // 주문 건수
    private Integer visitCount; // 방문자 수
    private Integer avgUsageTime;
}
