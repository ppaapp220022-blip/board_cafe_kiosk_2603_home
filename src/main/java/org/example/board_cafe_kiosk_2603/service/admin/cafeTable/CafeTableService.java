package org.example.board_cafe_kiosk_2603.service.admin.cafeTable;

import org.example.board_cafe_kiosk_2603.domain.admin.table.CafeTable;
import org.example.board_cafe_kiosk_2603.dto.admin.table.CafeTableDTO;
import org.example.board_cafe_kiosk_2603.dto.kiosk.order.OrderItemDTO;

import java.util.List;
import java.util.Optional;



/*
 * 작성자 : 강수연
 * 기능 : 관리자 테이블 관리 서비스 인터페이스
 * 날짜 : 2026-03-26
 */
public interface CafeTableService {
    /*
     * 작성자 : 강수연
     * 기능 : 전체 테이블 상태 조회
     * 날짜 : 2026-03-26
     */
    List<CafeTableDTO> getAllTableStatus();

    /*
     * 작성자 : 강수연
     * 기능 : 테이블 상태 변경
     * 날짜 : 2026-03-30
     */
    void changeTableStatus(Integer id, String status);

    /*
     * 작성자 : 서주연
     * 기능 : 테이블 접근 토큰 재발급
     * 날짜 : 2026-04-01
     */
    String generateNewToken(Integer id);

    /*
     * 작성자 : 서주연
     * 기능 : 영업일 시작 기준 전체 테이블 초기화
     * 날짜 : 2026-04-06
     */
    void resetAllTablesForNewDay();

    /*
     * 작성자 : 김민기
     * 기능 : 활성 주문 목록 조회
     * 날짜 : 2026-04-16
     */
    List<OrderItemDTO> getActiveOrders(Integer tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 미읽음 메시지 조회
     * 날짜 : 2026-03-30
     */
    List<String> getUnreadMessages(Integer tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 메시지 읽음 처리
     * 날짜 : 2026-03-30
     */
    void markMessagesAsRead(Integer tableId);

    /*
     * 작성자 : 서주연
     * 기능 : 테이블 로그인 처리
     * 날짜 : 2026-04-01
     */
    Optional<CafeTable> login(int tableNumber, String password);


    /*
     * 작성자 : 서주연
     * 기능 : 접근 토큰 갱신
     * 날짜 : 2026-04-06
     */
    void updateAccessToken(int tableId, String accessToken);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블 상태 조회
     * 날짜 : 2026-04-12
     */
    String getTableStatus(int tableId);

    /*
     * 작성자 : 서주연
     * 기능 : 현재 세션 ID 조회
     * 날짜 : 2026-04-06
     */
    Long findCurrentSessionId(int tableId);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 세션 조회
     * 날짜 : 2026-04-06
     */
    Long findActiveSessionByTableId(int tableId);

    /*
     * 작성자 : 서주연
     * 기능 : 테이블과 세션 상태 동기화
     * 날짜 : 2026-04-06
     */
    void syncTableWithSession(int tableId, Long sessionId);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블 접근 토큰 조회
     * 날짜 : 2026-04-16
     */
    String getTableAccessToken(int tableId);

}
