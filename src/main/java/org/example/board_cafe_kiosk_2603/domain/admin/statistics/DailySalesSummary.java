package org.example.board_cafe_kiosk_2603.domain.admin.statistics;

import lombok.*;

import java.time.LocalDate;

/*
 * 작성자 : 강수연
 * 기능 : DailySalesSummary 클래스
 * 날짜 : 2026-03-31
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesSummary {
    private LocalDate statDate;
    private Long totalRevenue;
    private Integer orderCount;
    private Integer visitCount;
    private Integer avgUsageTime;
}
