package org.example.board_cafe_kiosk_2603.mapper.admin.statistics;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.DailySalesDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.GameStatsDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.ItemSalesDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/*
 * 작성자 : 강수연
 * 기능 : 통계 데이터 접근 인터페이스
 * 날짜 : 2026-04-02
 */

@Mapper
public interface StatMapper {

    /*
     * 작성자 : 강수연
     * 기능 : 일일 요약 통계 삭제
     * 날짜 : 2026-04-02
     */
    void deleteDailySummary(@Param("targetDate") LocalDate targetDate);

    /*
     * 작성자 : 강수연
     * 기능 : 세션 기준 일일 요약 통계 생성
     * 날짜 : 2026-04-02
     */
    void insertDailySummaryFromSessions(@Param("targetDate") LocalDate targetDate);

    /*
     * 작성자 : 강수연
     * 기능 : 일일 상품 판매 통계 삭제
     * 날짜 : 2026-04-02
     */
    void deleteItemSalesHistory(@Param("targetDate") LocalDate targetDate);

    /*
     * 작성자 : 강수연
     * 기능 : 일일 상품 판매 통계 생성
     * 날짜 : 2026-04-02
     */
    void insertItemSalesHistory(@Param("targetDate") LocalDate targetDate);

    /*
     * 작성자 : 강수연
     * 기능 : 주간 매출 통계 조회
     * 날짜 : 2026-04-02
     */
    List<DailySalesDTO> getWeeklyStats(@Param("endDate") LocalDate endDate);

    /*
     * 작성자 : 강수연
     * 기능 : 일자별 인기 메뉴 조회
     * 날짜 : 2026-04-02
     */
    List<ItemSalesDTO> getTopSellingMenuByDate(@Param("targetDate") LocalDate targetDate,
                                               @Param("limit") int limit);

    /*
     * 작성자 : 강수연
     * 기능 : 최근 인기 상품 조회
     * 날짜 : 2026-04-02
     */
    List<ItemSalesDTO> getTopSellingItems();

    /*
     * 작성자 : 강수연
     * 기능 : 일자별 카테고리 매출 통계 조회
     * 날짜 : 2026-04-02
     */
    List<Map<String, Object>> getCategoryStatsByDate(@Param("targetDate") LocalDate targetDate);

    /*
     * 작성자 : 강수연
     * 기능 : 월간 인기 보드게임 조회
     * 날짜 : 2026-04-02
     */
    List<GameStatsDTO> getTopGamesByMonth(@Param("targetDate") LocalDate targetDate,
                                          @Param("limit") int limit);
}

