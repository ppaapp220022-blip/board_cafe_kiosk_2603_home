package org.example.board_cafe_kiosk_2603.controller.admin.macro;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.macro.MacroMessageResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;
import org.example.board_cafe_kiosk_2603.service.admin.macro.MacroMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * 작성자 : 강수연
 * 기능 : MacroMessage 관련 요청을 처리하는 컨트롤러
 * 날짜 : 2026-03-25
 */

@Log4j2
@Controller
@RequestMapping("/admin/macro")
@RequiredArgsConstructor
public class MacroMessageController {
    private final MacroMessageService macroMessageService;

    /*
     * 작성자 : 강수연
     * 기능 : getAllMacro 메서드
     * 날짜 : 2026-03-25
     */

    @GetMapping
    public String getAllMacro(Model model) {
        log.info("--- MacroMessageController getAllMacro get ---");
        List<MacroMessageResponseDTO> macroList = macroMessageService.getAllActiveMessages();

        // direction별로 그룹핑 (예: "STAFF_TO_TABLE" -> 리스트, "CUSTOMER_TO_STAFF" -> 리스트)
        Map<String, List<MacroMessageResponseDTO>> macroGroups = macroList.stream()
                .collect(Collectors.groupingBy(MacroMessageResponseDTO::getDirection));

        model.addAttribute("macroGroups", macroGroups);
        return "admin/macro";
    }
    /*
     * 작성자 : 강수연
     * 기능 : 직원용 테이블 매크로 목록 조회
     * 날짜 : 2026-04-08
     */

    @ResponseBody // HTML이 아닌 JSON 데이터로 반환하겠다는 의미
    @GetMapping("/api")
    public ResponseEntity<List<MacroMessageResponseDTO>> getStaffToTableMacros(
            @RequestParam(required = false, defaultValue = "STAFF_TO_TABLE") String direction) {

        List<MacroMessageResponseDTO> allMessages = macroMessageService.getAllActiveMessages();
        List<MacroMessageResponseDTO> filtered = allMessages.stream()
                .filter(msg -> msg.getDirection().equals(direction))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filtered);
    }
    /*
     * 작성자 : 강수연
     * 기능 : 메세지 전송 API
     * 날짜 : 2026-04-08
     */

    @PostMapping("/api/send")
    public ResponseEntity<?> send(@RequestBody Map<String, Object> data) {
        // Object로 꺼낸 뒤 문자열로 변환
        Object tableIdObj = data.get("tableId");
        Integer macroId = Integer.parseInt(data.get("macroMessageId").toString());

        if (tableIdObj == null) {
            return ResponseEntity.badRequest().body("테이블 정보가 없습니다.");
        }

        String tableIdStr = tableIdObj.toString();

        // "ALL"인지 체크
        if ("ALL".equals(tableIdStr)) {
            log.info("📢 전체 테이블 메세지 전송 요청 (매크로 ID: {})", macroId);
            macroMessageService.sendToAllActiveTables(macroId);
        } else {
            // 숫자인 경우 기존처럼 단일 전송
            try {
                Integer tableId = Integer.parseInt(tableIdStr);
                log.info("✅ 단일 테이블 메세지 전송 요청 (테이블: {}, 매크로: {})", tableId, macroId);
                macroMessageService.sendMessage(tableId, macroId);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("잘못된 테이블 형식입니다.");
            }
        }

        return ResponseEntity.ok().build();
    }
    /*
     * 작성자 : 강수연
     * 기능 : 매크로 등록 API
     * 날짜 : 2026-04-08
     */

    @ResponseBody
    @PostMapping("/api/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER')")
    public ResponseEntity<?> createMacro(@RequestBody Map<String, String> data) {
        String direction = data.get("direction");
        String messageText = data.get("messageText");

        if (direction == null || messageText == null || messageText.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("입력값이 올바르지 않습니다.");
        }

        macroMessageService.createMacro(direction, messageText);
        return ResponseEntity.ok(Map.of("message", "성공적으로 등록되었습니다."));
    }
    /*
     * 작성자 : 강수연
     * 기능 : 매크로 삭제 API (Soft Delete)
     * 날짜 : 2026-04-08
     */

    @ResponseBody
    @DeleteMapping("/api/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER')")
    public ResponseEntity<?> deleteMacro(@PathVariable Integer id) {
        macroMessageService.deleteMacro(id);
        return ResponseEntity.ok(Map.of("message", "삭제 처리되었습니다."));
    }
    /*
     * 작성자 : 서민성
     * 기능 : 탭별 페이징 AJAX
     * 날짜 : 2026-04-09
     */

    @GetMapping("/list")
    @ResponseBody
    public PageResponseDTO<MacroMessageResponseDTO> getPagedList(
            @RequestParam String direction,
            PageRequestDTO pageRequestDTO) {
        return macroMessageService.getPagedMessage(direction, pageRequestDTO);
    }
}
