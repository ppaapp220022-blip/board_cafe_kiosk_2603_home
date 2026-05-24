package org.example.board_cafe_kiosk_2603.mapper.admin.table;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.common.cafeTableSession.CafeTableSession;
import org.example.board_cafe_kiosk_2603.domain.admin.table.CafeTable;
import org.example.board_cafe_kiosk_2603.dto.kiosk.order.OrderItemDTO;

import java.util.List;
import java.util.Optional;

@Mapper

/*
 * 작성자 : 강수연
 * 기능 : 테이블 및 세션 상태 데이터 접근 인터페이스
 * 날짜 : 2026-03-26
 */
public interface CafeTableMapper {
    /*
     * 작성자 : 강수연
     * 기능 : 전체 테이블 상태 조회
     * 날짜 : 2026-03-26
     */
    List<CafeTable> selectAllTables();


    /*
     * 작성자 : 강수연
     * 기능 : 사용 중인 테이블 ID 목록 조회
     * 날짜 : 2026-04-08
     * 현재 이용 중인(OCCUPIED) 테이블의 ID 리스트만 조회
     */

    /*
     * 작성자 : 강수연
     * 기능 : selectOccupiedTableIds 메서드
     * 날짜 : 2026-04-08
     */
    List<Integer> selectOccupiedTableIds();

    /*
     * 작성자 : 강수연
     * 기능 : 테이블 액세스 토큰 조회
     * 날짜 : 2026-03-26
     */
    String selectAccessTokenById(@Param("id") Integer id);

    /*
     * 작성자 : 강수연
     * 기능 : 신규 세션 등록
     * 날짜 : 2026-03-26
     */
    int insertNewSession(CafeTableSession session);

    /*
     * 작성자 : 강수연
     * 기능 : 테이블 상태와 현재 세션 동기화
     * 날짜 : 2026-03-26
     */
    int updateTableStatusAndSession(@Param("id") Integer id,
                                    @Param("status") String status,
                                    @Param("sessionId") Long sessionId);

    /*
     * 작성자 : 강수연
     * 기능 : 세션 종료 처리
     * 날짜 : 2026-03-26
     */
    int closeSession(@Param("sessionId") Long sessionId);

    /*
     * 작성자 : 강수연
     * 기능 : 현재 세션 ID 조회
     * 날짜 : 2026-03-26
     */
    Long selectCurrentSessionId(@Param("tableId") Integer tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 테이블 상태 조회
     * 날짜 : 2026-03-26
     */
    String selectStatusById(@Param("tableId") Integer tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 테이블 액세스 토큰 갱신
     * 날짜 : 2026-03-26
     */
    int updateAccessToken(@Param("tableId") int tableId, @Param("accessToken") String accessToken);

    /*
     * 작성자 : 강수연
     * 기능 : 활성 세션 ID 조회
     * 날짜 : 2026-03-26
     */
    Long selectActiveSessionByTableId(@Param("tableId") Integer tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 최근 세션 ID 조회
     * 날짜 : 2026-03-30
     */
    Long selectLatestSessionByTableId(@Param("tableId") Integer tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 전체 테이블 자정 초기화
     * 날짜 : 2026-03-26
     */
    int resetAllTablesAtMidnight();

    /*
     * 작성자 : 강수연
     * 기능 : 전체 활성 세션 종료
     * 날짜 : 2026-03-26
     */
    int updateAllActiveSessions();

    /*
     * 작성자 : 강수연
     * 기능 : 세션 기준 진행 중 주문 항목 조회
     * 날짜 : 2026-03-30
     */
    List<OrderItemDTO> selectActiveOrderItems(@Param("sessionId") Long sessionId);

    /*
     * 작성자 : 강수연
     * 기능 : 테이블 메시지 읽음 처리
     * 날짜 : 2026-03-30
     */
    int updateMessagesReadStatus(@Param("tableId") Integer tableId);

    /*
     * 작성자 : 강수연
     * 기능 : 읽지 않은 메시지 내용 조회
     * 날짜 : 2026-03-30
     */
    List<String> selectUnreadMessageContents(@Param("tableId") Integer tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블 번호로 단건 조회
     * 날짜 : 2026-03-27
     */
    Optional<CafeTable> findByTableNumber(@Param("tableNumber") int tableNumber);

    /*
     * 작성자 : 김민기
     * 기능 : 세션 기준 메시지 읽음 처리
     * 날짜 : 2026-04-12
     */
    int updateMessagesReadStatusBySessionId(@Param("sessionId") Long sessionId);

    /*
     * 작성자 : 김민기
     * 기능 : 전체 메시지 읽음 처리
     * 날짜 : 2026-04-12
     */
    int updateAllMessagesReadStatus();
}

