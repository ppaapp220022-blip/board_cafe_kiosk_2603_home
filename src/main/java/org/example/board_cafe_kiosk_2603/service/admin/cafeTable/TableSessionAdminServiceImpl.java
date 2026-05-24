package org.example.board_cafe_kiosk_2603.service.admin.cafeTable;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.common.cafeTableSession.CafeTableSession;
import org.example.board_cafe_kiosk_2603.mapper.common.cafeTableSession.CafeTableSessionMapper;
import org.springframework.stereotype.Service;

/*
 * 작성자 : 서주연
 * 기능 : TableSessionAdmin 관련 비즈니스 로직을 처리하는 서비스 구현체
 * 날짜 : 2026-03-31
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class TableSessionAdminServiceImpl implements TableSessionAdminService {

    private final CafeTableSessionMapper tableSessionAdminMapper;
    /*
     * 작성자 : 서주연
     * 기능 : 테이블 ID로 활성 세션 조회
     * 날짜 : 2026-03-31
     */

    @Override
    public CafeTableSession getActiveSession(int tableId) {
        CafeTableSession tableSession = tableSessionAdminMapper.findActiveByTableId(tableId);
        log.info("활성 세션 조회... tableId: {}, session: {}", tableId, tableSession);
        return tableSession;
    }
    /*
     * 작성자 : 서주연
     * 기능 : 세션 종료 (퇴장 처리)
     * 날짜 : 2026-03-31
     */

    @Override
    public void closeSession(int tableId) {
        CafeTableSession cafeTableSession = tableSessionAdminMapper.findActiveByTableId(tableId);
        if (cafeTableSession == null) {
            log.warn("종료할 활성 세션이 없습니다 - tableId: {}", tableId);
            return;
        }
        CafeTableSession closedSession = CafeTableSession.builder()
                .id(cafeTableSession.getId())
                .tableId(cafeTableSession.getTableId())
                .packageId(cafeTableSession.getPackageId())
                .initialGuestCnt(cafeTableSession.getInitialGuestCnt())
                .isActive(false)
                .totalAmount(cafeTableSession.getTotalAmount())
                .build();

        tableSessionAdminMapper.checkOut(closedSession);
        log.info("세션 종료 완료... tableId: {}", tableId);
    }
    /*
     * 작성자 : 서주연
     * 기능 : 최종 정산 금액 업데이트
     * 날짜 : 2026-03-31
     */

    @Override
    public void updateTotalAmount(Long sessionId, int totalAmount) {
        tableSessionAdminMapper.updateTotalAmount(sessionId, totalAmount);
        log.info("정산 금액 업데이트 완료... sessionId: {}, totalAmount: {}", sessionId, totalAmount);
    }
}
