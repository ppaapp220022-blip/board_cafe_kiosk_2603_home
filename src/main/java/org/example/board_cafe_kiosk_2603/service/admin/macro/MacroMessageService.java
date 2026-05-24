package org.example.board_cafe_kiosk_2603.service.admin.macro;

import org.example.board_cafe_kiosk_2603.dto.admin.macro.MacroMessageResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;

import java.util.List;


/*
 * 작성자 : 강수연
 * 기능 : 매크로 메시지 서비스 인터페이스
 * 날짜 : 2026-04-08
 */
public interface MacroMessageService {
    /*
     * 작성자 : 강수연
     * 기능 : 활성 매크로 메시지 조회
     * 날짜 : 2026-04-08
     */
    List<MacroMessageResponseDTO> getAllActiveMessages();

    /*
     * 작성자 : 서민성
     * 기능 : 단일 테이블로 메시지 전송
     * 날짜 : 2026-04-09
     */
    void sendMessage(Integer tableId, Integer macroId);

    /*
     * 작성자 : 강수연
     * 기능 : 전체 사용 중 테이블로 메시지 전송
     * 날짜 : 2026-04-08
     */
    void sendToAllActiveTables(Integer macroId);

    /*
     * 작성자 : 강수연
     * 기능 : 매크로 메시지 등록
     * 날짜 : 2026-04-08
     */
    void createMacro(String direction, String messageText);

    /*
     * 작성자 : 강수연
     * 기능 : 매크로 메시지 비활성화
     * 날짜 : 2026-04-08
     */
    void deleteMacro(Integer id);

    /*
     * 작성자 : 서민성
     * 기능 : 매크로 메시지 페이징 조회
     * 날짜 : 2026-04-09
     */
    PageResponseDTO<MacroMessageResponseDTO> getPagedMessage(String direction, PageRequestDTO pageRequestDTO);
}
