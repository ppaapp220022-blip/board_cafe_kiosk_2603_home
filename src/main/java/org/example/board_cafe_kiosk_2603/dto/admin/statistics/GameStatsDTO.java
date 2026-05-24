package org.example.board_cafe_kiosk_2603.dto.admin.statistics;

import lombok.*;

/*
 * 작성자 : 강수연
 * 기능 : GameStats 데이터 전달 객체
 * 날짜 : 2026-04-07
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GameStatsDTO {
    private String gameName;
    private int rentCount;
}
