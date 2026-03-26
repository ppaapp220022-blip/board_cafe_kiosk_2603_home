package org.example.board_cafe_kiosk_2603.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.CafeTableDTO;
import org.example.board_cafe_kiosk_2603.service.admin.cafeTable.CafeTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class TableController {
    private final CafeTableService cafeTableService;

    @GetMapping
    public String dashboard(Model model) {
        /* 주 설명: 서비스에서 전체 테이블 리스트를 가져와서 뷰에 전달 */
        List<CafeTableDTO> tables = cafeTableService.getAllTableStatus();
        model.addAttribute("tables", tables);

        return "admin/dashboard";
    }

    /**
     * [PATCH] 테이블 상태 변경 (입실/퇴실/청소)
     * @Param id: 테이블 PK
     * @RequestBody request: {"status": "OCCUPIED"} 형식
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") Integer id, @RequestBody Map<String, String> request) {
        /* 주 설명: 특정 테이블의 이용 상태를 변경하고 입실 시간을 기록함 */
        String status = request.get("status");
        cafeTableService.changeTableStatus(id, status);

        log.info("관리자 요청: 테이블 {}번 상태를 {}로 변경", id, status);
        return ResponseEntity.noContent().build();
        // 상세 설명: 상태값이 'EMPTY'일 경우 DB 내부 로직에 의해 시간도 자동 초기화됨
    }

    /**
     * [POST] 특정 테이블의 액세스 토큰 갱신
     * @Param id: 테이블 PK
     */
    @PostMapping("/{id}/token")
    public ResponseEntity<Map<String, String>> refreshToken(@PathVariable("id") Integer id) {
        /** * [핵심] 보안 세션 갱신을 위한 독립적인 토큰 발급 로직
         * 상태나 시간의 변경 없이 오직 access_token 필드만 업데이트함
         */
        String newToken = cafeTableService.generateNewToken(id);

        return ResponseEntity.ok(Map.of("accessToken", newToken));
        // 상세 설명: 새로 생성된 8자리(또는 UUID) 토큰을 클라이언트에 반환
    }

    /**
     * [DELETE] 자정 리셋 수동 실행 (테스트용 또는 비상용)
     * 주 설명: AM 12:00 스케줄러와 동일한 로직을 즉시 실행함
     */
    @DeleteMapping("/reset")
    public ResponseEntity<String> forceReset() {
        /* 주 설명: 모든 테이블을 강제로 EMPTY 및 토큰 초기화 상태로 만듦 */
        cafeTableService.resetAllTablesForNewDay();

        log.warn("관리자에 의한 전체 테이블 강제 초기화 실행됨");
        return ResponseEntity.ok("All tables have been reset.");
        // 상세 설명: 영업 마감 후 수동으로 데이터를 클린하게 비울 때 사용
    }
}

