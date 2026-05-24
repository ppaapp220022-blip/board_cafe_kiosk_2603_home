package org.example.board_cafe_kiosk_2603.mapper.admin.macro;

import org.apache.ibatis.annotations.Mapper;
import org.example.board_cafe_kiosk_2603.domain.admin.macro.AdminTableMessage;


@Mapper

/*
 * 작성자 : 강수연
 * 기능 : 관리자 테이블 메시지 데이터 접근 인터페이스
 * 날짜 : 2026-04-08
 */
public interface AdminTableMessageMapper {
    /*
     * 작성자 : 강수연
     * 기능 : 테이블 메시지 등록
     * 날짜 : 2026-04-08
     */
    void insertMessage(AdminTableMessage adminTableMessage);
}
