package org.example.board_cafe_kiosk_2603.service.admin.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.DailySalesDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.statistics.ItemSalesDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.statistics.StatMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class StatService {
    private final StatMapper statMapper;

    /**
     * 특정 날짜의 모든 통계 데이터를 갱신 (일일 요약 + 상품별 히스토리)
     */
    public void createDailyStatistics(LocalDate targetDate) {
        log.info("=== StatService createDailyStatistics ===");
        log.info("{} 날짜의 통계 데이터 생성 시작", targetDate);

        // 1. 일일 매출 요약 (기존 데이터 삭제 후 삽입)
        statMapper.deleteDailySummary(targetDate);
        statMapper.insertDailySummaryFromSessions(targetDate);

        // 2. 상품별 판매 히스토리 (기존 데이터 삭제 후 삽입)
        statMapper.deleteItemSalesHistory(targetDate);
        statMapper.insertItemSalesHistory(targetDate);

        log.info("{} 날짜의 통계 데이터 갱신 완료", targetDate);
    }

    /**
     * 특정 기간(예: 최근 한 달)의 통계를 한 번에 초기화할 때 사용
     */
    @Transactional
    public void createStatisticsForPeriod(LocalDate startDate, LocalDate endDate) {
        log.info("=== StatService createStatisticsForPeriod ===");
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            createDailyStatistics(current);
            current = current.plusDays(1);
        }
    }

    /**
     * 어제 날짜를 자동으로 계산해서 호출
     */
    public void createYesterdayStatistics() {
        log.info("=== StatService createYesterdayStatistics ===");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        createDailyStatistics(yesterday);
    }

    /**
     * 기준 날짜 포함 최근 7일간의 통계 데이터 조회
     */
    public List<DailySalesDTO> getWeeklyStats(LocalDate endDate) {
        return statMapper.getWeeklyStats(endDate);
    }

    /**
     * 특정 날짜의 인기 메뉴 조회
     */
    public List<ItemSalesDTO> getTopSellingMenuByDate(LocalDate targetDate, int limit) {
        return statMapper.getTopSellingMenuByDate(targetDate, limit);
    }

    public Map<String, Object> getCategoryStats(LocalDate targetDate) {
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
}
