package org.example.board_cafe_kiosk_2603.mapper.kiosk.tableMessage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.kiosk.tableMessage.TableMessage;

import java.util.List;


@Mapper

/*
 * 작성자 : 김민기
 * 기능 : 테이블 요청 메시지 데이터 접근 인터페이스
 * 날짜 : 2026-03-30
 */
public interface TableMessageMapper {

    /*
     * 작성자 : 김민기
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-12
     */
    void insert(TableMessage message);

    /*
     * 작성자 : 김민기
     * 기능 : findUnread 처리
     * 날짜 : 2026-03-30
     */
    List<TableMessage> findUnread();

    /*
     * 작성자 : 김민기
     * 기능 : 테이블 ID 기준 조회
     * 날짜 : 2026-03-30
     */
    List<TableMessage> findByTableId(@Param("tableId") int tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블의 미읽음 관리자 메시지 조회
     * 날짜 : 2026-04-12
     */
    List<TableMessage> findUnreadStaffByTableId(@Param("tableId") int tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 메시지 읽음 처리
     * 날짜 : 2026-03-30
     */
    void markAsRead(@Param("id") long id);

    /*
     * 작성자 : 김민기
     * 기능 : 전체 메시지 읽음 처리
     * 날짜 : 2026-03-30
     */
    void markAllAsRead();
}

