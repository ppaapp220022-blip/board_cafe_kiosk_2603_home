package org.example.board_cafe_kiosk_2603.service.admin.cafeTable;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.kiosk.TableSession;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.TableSessionMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TableSessionAdminServiceImpl implements TableSessionAdminService {

    private final TableSessionMapper  tableSessionMapper;

    /*
     * 테이블 ID로 활성 세션 조회
     * 관리자 대시보드에서 현재 이용 중인 테이블 현황 확인용
     */
    @Override
    public TableSession getActiveSession(int tableId) {
        TableSession tableSession = tableSessionMapper.findActiveByTableId(tableId);
        log.info("활성 세션 조회... tableId: {}, session: {}", tableId, tableSession);
        return tableSession;
    }

    /*
     * 세션 종료 (퇴장 처리)
     * check_out_time 기록 + is_active = false
     */
    @Override
    public void closeSession(int tableId) {
        TableSession activeSession = tableSessionMapper.findActiveByTableId(tableId);
        if (activeSession == null) {
            log.warn("종료할 활성 세션이 없습니다 - tableId: {}", tableId);
            return;
        }
        TableSession closedSession = TableSession.builder()
                .id(activeSession.getId())
                .tableId(activeSession.getTableId())
                .packageId(activeSession.getPackageId())
                .initialGuestCnt(activeSession.getInitialGuestCnt())
                .isActive(false)
                .totalAmount(activeSession.getTotalAmount())
                .build();

        tableSessionMapper.checkOut(closedSession);
        log.info("세션 종료 완료... tableId: {}", tableId);
    }

    /*
     * 최종 정산 금액 업데이트
     * 퇴장 시 총 결제 금액을 세션에 기록
     */
    @Override
    public void updateTotalAmount(long sessionId, int totalAmount) {
        tableSessionMapper.updateTotalAmount(sessionId, totalAmount);
        log.info("정산 금액 업데이트 완료... sessionId: {}, totalAmount: {}", sessionId, totalAmount);
    }
}
