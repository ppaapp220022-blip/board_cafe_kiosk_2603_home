package org.example.board_cafe_kiosk_2603.mapper.admin.statistics;

import org.apache.ibatis.annotations.Mapper;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.DailySalesDTO;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatMapper {
    /** [Batch용] 1-1. 특정 날짜의 기존 일별 요약 데이터 삭제 **/
    void deleteDailySummary(LocalDate targetDate);

    /** [Batch용] 1-2. 특정 날짜의 세션/주문을 집계하여 요약 테이블에 삽입 **/
    void insertDailySummaryFromSessions(LocalDate targetDate);

    /** [Batch용] 2-1. 특정 날짜의 기존 상품 판매 이력 삭제 **/
    void deleteItemSalesHistory(LocalDate targetDate);

    /** [Batch용] 2-2. 특정 날짜의 메뉴별 판매 수량/금액을 집계하여 삽입 **/
    void insertItemSalesHistory(LocalDate targetDate);

    /** [화면조회용] 최근 7일간의 매출 통계 조회 (DTO 사용) **/
    List<DailySalesDTO> getWeeklyStats();
}
