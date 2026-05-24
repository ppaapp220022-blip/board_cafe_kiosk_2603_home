package org.example.board_cafe_kiosk_2603.mapper.admin.macro;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.macro.MacroMessage;

import java.util.List;


@Mapper

/*
 * 작성자 : 강수연
 * 기능 : 매크로 메시지 데이터 접근 인터페이스
 * 날짜 : 2026-04-08
 */
public interface MacroMessageMapper {
    /*
     * 작성자 : 서민성
     * 기능 : 활성 데이터 목록 조회
     * 날짜 : 2026-04-09
     */
    List<MacroMessage> findAllActive();

    /*
     * 작성자 : 서민성
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-04-09
     */
    MacroMessage findById(Integer id);

    /*
     * 작성자 : 강수연
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-08
     */
    void insertMacro(MacroMessage macroMessage);

    /*
     * 작성자 : 강수연
     * 기능 : 매크로 비활성화
     * 날짜 : 2026-04-08
     */
    void deactivateMacro(Integer id);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 목록 조회
     * 날짜 : 2026-04-09
     */
    List<MacroMessage> selectList(@Param("direction") String direction,
                                  @Param("skip") int skip,
                                  @Param("size") int size);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 건수 조회
     * 날짜 : 2026-04-09
     */
    int selectCount(@Param("direction") String direction);
}
