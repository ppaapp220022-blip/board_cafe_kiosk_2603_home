package org.example.board_cafe_kiosk_2603.service.admin.cafeTable;

import org.example.board_cafe_kiosk_2603.domain.common.cafeTableSession.CafeTableSession;



/*
 * 작성자 : 서주연
 * 기능 : 관리자 테이블 세션 관리 서비스 인터페이스
 * 날짜 : 2026-03-31
 */
public interface TableSessionAdminService {
    /*
     * 작성자 : 서주연
     * 기능 : 활성 세션 조회
     * 날짜 : 2026-03-31
     */
    CafeTableSession getActiveSession(int tableId);

    /*
     * 작성자 : 서주연
     * 기능 : 세션 종료 처리
     * 날짜 : 2026-03-31
     */
    void closeSession(int tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 총 결제 금액 수정
     * 날짜 : 2026-03-31
     */
    void updateTotalAmount(Long sessionId, int totalAmount);
}
