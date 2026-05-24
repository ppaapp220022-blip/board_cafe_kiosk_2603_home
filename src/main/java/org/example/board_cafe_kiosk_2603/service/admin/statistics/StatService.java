package org.example.board_cafe_kiosk_2603.service.admin.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.DailySalesDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.GameStatsDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.ItemSalesDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.statistics.StatMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * 작성자 : 강수연
 * 기능 : Stat 서비스 인터페이스
 * 날짜 : 2026-04-02
 */

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class StatService {
    private final StatMapper statMapper;

    public void createDailyStatistics(LocalDate targetDate) {
        log.info("--- StatService createDailyStatistics ---");
        log.info("{} 날짜 통계 데이터 재집계", targetDate);

        // 1. 일일 매출 요약 (기존 데이터 삭제 후 JOIN 기반 재삽입)
        // SQL 내부: ts.initial_guest_cnt + 추가인원 수량 합산 처리됨
        statMapper.deleteDailySummary(targetDate);
        statMapper.insertDailySummaryFromSessions(targetDate);

        // 2. 상품별 판매 히스토리 (기존 데이터 삭제 후 재삽입)
        // SQL 내부: menu.name != '인원 추가 (1명)' 조건으로 순수 메뉴 실적만 저장됨
        statMapper.deleteItemSalesHistory(targetDate);
        statMapper.insertItemSalesHistory(targetDate);

        log.info("{} 날짜 통계 갱신 완료", targetDate);
    }
    public void createStatisticsForPeriod(LocalDate startDate, LocalDate endDate) {
        log.info("--- StatService createStatisticsForPeriod ---");
        log.info("{} 부터 {} 까지 기간 통계 생성", startDate, endDate);
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            createDailyStatistics(current);
            current = current.plusDays(1);
        }
    }
    @Transactional(readOnly = true)
    public List<DailySalesDTO> getWeeklyStats(LocalDate endDate) {
        log.info("--- StatService getWeeklyStats ---");

        return statMapper.getWeeklyStats(endDate);
    }
    @Transactional(readOnly = true)
    public List<ItemSalesDTO> getTopSellingMenuByDate(LocalDate targetDate, int limit) {
        log.info("--- StatService getTopSellingMenuByDate ---");

        return statMapper.getTopSellingMenuByDate(targetDate, limit);
    }
    @Transactional(readOnly = true)
    public Map<String, Object> getCategoryStats(LocalDate targetDate) {
        log.info("--- StatService getCategoryStats ---");

        // 이미 '인원 추가' 금액이 제외된 item_sales_history 기반으로 조회됨
        List<Map<String, Object>> stats = statMapper.getCategoryStatsByDate(targetDate);

        List<String> labels = stats.stream()
                .map(s -> String.valueOf(s.get("categoryName")))
                .collect(Collectors.toList());

        List<Long> values = stats.stream()
                .map(s -> Long.parseLong(String.valueOf(s.get("totalAmount"))))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("values", values);
        return result;
    }
    @Transactional(readOnly = true)
    public List<GameStatsDTO> getTopGamesByMonth(LocalDate targetDate, int limit) {
        log.info("--- StatService getTopGamesByMonth ---");
        return statMapper.getTopGamesByMonth(targetDate, limit);
    }
}
