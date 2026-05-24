package org.example.board_cafe_kiosk_2603.mapper.common.cafeTableSession;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.common.cafeTableSession.CafeTableSession;

/*
 * 작성자 : 강수연
 * 기능 : 테이블 세션 데이터 접근 인터페이스
 * 날짜 : 2026-03-31
 */
@Mapper
public interface CafeTableSessionMapper {

    /*
     * 작성자 : 강수연
     * 기능 : 테이블의 활성 세션 조회
     * 날짜 : 2026-03-31
     */
    CafeTableSession findActiveByTableId(int tableId);

    /*
     * 작성자 : 강수연
     * 기능 : ID로 세션 단건 조회
     * 날짜 : 2026-03-31
     */
    CafeTableSession findById(long id);

    /*
     * 작성자 : 강수연
     * 기능 : 세션 등록
     * 날짜 : 2026-03-31
     */
    void insert(CafeTableSession session);

    /*
     * 작성자 : 강수연
     * 기능 : 세션 체크아웃 처리
     * 날짜 : 2026-03-31
     */
    void checkOut(CafeTableSession session);

    /*
     * 작성자 : 강수연
     * 기능 : 세션 총 결제 금액 수정
     * 날짜 : 2026-03-31
     */
    void updateTotalAmount(@Param("id") long id, @Param("totalAmount") int totalAmount);
}
