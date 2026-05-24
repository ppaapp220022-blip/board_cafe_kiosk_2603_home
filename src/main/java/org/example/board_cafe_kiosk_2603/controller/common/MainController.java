package org.example.board_cafe_kiosk_2603.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.example.board_cafe_kiosk_2603.domain.common.cafeTableSession.CafeTableSession;
import org.example.board_cafe_kiosk_2603.service.admin.cafeTable.CafeTableService;
import org.example.board_cafe_kiosk_2603.service.admin.cafeTable.TableSessionAdminService;
import org.example.board_cafe_kiosk_2603.service.kiosk.tableSession.TableSessionKioskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/*
 * 작성자 : 서주연
 * 기능 : Main 관련 요청을 처리하는 컨트롤러
 * 날짜 : 2026-03-23
 */

@Log4j2
@Controller
@RequiredArgsConstructor
public class MainController {

    private final TableSessionAdminService tableSessionAdminService;
    private final TableSessionKioskService tableSessionKioskService;
    private final CafeTableService cafeTableService;
    /*
     * 작성자 : 서주연
     * 기능 : 루트 경로 리다이렉트
     * 날짜 : 2026-03-23
     */


    @GetMapping("/")
    public String root() {
        return "redirect:/common/login";
    }

    /*
     * 작성자 : 서주연
     * 기능 : loginPage 메서드
     * 날짜 : 2026-03-23
     */

    @GetMapping("/common/login")
    public String loginPage() {
        return "common/login";
    }

    /*
     * 작성자 : 서주연
     * 기능 : adminLoginPage 메서드
     * 날짜 : 2026-03-23
     */

    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "login/admin_login";
    }

    /*
     * 작성자 : 서주연
     * 기능 : kioskLoginPage 메서드
     * 날짜 : 2026-03-23
     */

    @GetMapping("/kiosk/login")
    public String kioskLoginPage(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return "login/kiosk_login";
    }
    /*
     * 작성자 : 서주연
     * 기능 : 키오스크 강제 로그아웃 처리
     * 날짜 : 2026-04-01
     */

    @GetMapping("/kiosk/force-logout")
    public String kioskForceLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("강제 로그아웃 - 테이블: {}", session.getAttribute("tableNumber"));
            session.invalidate();
        }
        return "redirect:/kiosk/login";
    }
    /*
     * 작성자 : 서주연
     * 기능 : 키오스크 인원수 저장 처리
     * 날짜 : 2026-03-23
     */

    @PostMapping("/kiosk/headcount")
    public String headcountProcess(@RequestParam int partySize,
                                   HttpSession session) {
        session.setAttribute("partySize", partySize);
        return "redirect:/kiosk/phone_login";
    }
    /*
     * 작성자 : 서주연
     * 기능 : [2단계] 전화번호 처리 - customerPhone 세션 저장
     * 날짜 : 2026-03-23
     */

    @PostMapping("/kiosk/phone_login")
    public String phoneLoginProcess(@RequestParam(required = false) String phone,
                                    HttpSession session) {
        if (phone != null && !phone.isEmpty()) {
            session.setAttribute("customerPhone", phone);
        } else {
            session.removeAttribute("customerPhone");
        }
        return "redirect:/kiosk/package_selection";
    }
    /*
     * 작성자 : 서주연
     * 기능 : [3단계] 패키지 선택 처리 - table_session DB 저장
     * 날짜 : 2026-03-23
     */

    @PostMapping("/kiosk/package_selection")
    public String packageSelectionProcess(@RequestParam int packageId,
                                          HttpSession session) {
        int tableId   = (int) session.getAttribute("tableId");
        int partySize = (int) session.getAttribute("partySize");

        // 이미 활성 세션이 있으면 새로 생성하지 않고 기존 세션 정보 유지
        CafeTableSession activeSession = tableSessionAdminService.getActiveSession(tableId);
        if (activeSession != null) {
            session.setAttribute("partySize", activeSession.getInitialGuestCnt());
            session.setAttribute("packageId", activeSession.getPackageId()); // ★ 추가
            log.info("기존 활성 세션 존재 — 기존 인원수: {}명, packageId: {}",
                    activeSession.getInitialGuestCnt(), activeSession.getPackageId());
            return "redirect:/kiosk/drinks";
        }

        // 활성 세션 없으면 신규 생성 후 cafe_table 동기화
        Long newSessionId = tableSessionKioskService.createSession(tableId, packageId, partySize);
        cafeTableService.syncTableWithSession(tableId, newSessionId);

        // ★ 추가: packageId를 세션에 저장
        //    기존 코드에서 누락되어 buildMenuModel()의 packageId가 항상 null이었음
        session.setAttribute("packageId", packageId);

        log.info("table_session 생성 + cafe_table 동기화 완료 — tableId: {}, packageId: {}, partySize: {}, sessionId: {}",
                tableId, packageId, partySize, newSessionId);

        return "redirect:/kiosk/drinks";
    }
    /*
     * 작성자 : 서주연
     * 기능 : [4단계] 메인 메뉴 페이지
     * 날짜 : 2026-03-23
     */

    @GetMapping("/kiosk/menu")
    public String mainMenuPage(HttpSession session,
                               HttpServletResponse response) throws IOException {
        Object tableIdObj = session.getAttribute("tableId");
        if (!(tableIdObj instanceof Integer tableId)) {
            log.warn("--- [MainController] /kiosk/menu 진입 시 tableId 없음 → /kiosk/session/start 리다이렉트 ---");
            response.sendRedirect("/kiosk/session/start");
            return null;
        }

        Long recoverSessionId = cafeTableService.findActiveSessionByTableId(tableId);
        String tableStatus = cafeTableService.getTableStatus(tableId);
        Long currentSessionId = cafeTableService.findCurrentSessionId(tableId);
        if (recoverSessionId != null &&
                (!"OCCUPIED".equals(tableStatus) || currentSessionId == null || !recoverSessionId.equals(currentSessionId))) {
            cafeTableService.syncTableWithSession(tableId, recoverSessionId);
            tableStatus = "OCCUPIED";
            log.warn("--- [MainController] 메뉴 진입 전 상태/세션 자동 복구 완료 (tableId: {}, sessionId: {}) ---",
                    tableId, recoverSessionId);
        }

        if (!"OCCUPIED".equals(tableStatus)) {
            log.warn("--- [MainController] /kiosk/menu 진입 차단: 대시보드 상태가 OCCUPIED 아님 (tableId: {}, status: {}) ---",
                    tableId, tableStatus);
            session.removeAttribute("partySize");
            session.removeAttribute("packageId");
            session.removeAttribute("sessionStartTime");
            session.removeAttribute("durationMinutes");
            response.sendRedirect("/kiosk/session/start");
            return null;
        }

        CafeTableSession activeSession = tableSessionAdminService.getActiveSession(tableId);
        if (activeSession == null) {
            if (recoverSessionId != null) {
                cafeTableService.syncTableWithSession(tableId, recoverSessionId);
                activeSession = tableSessionAdminService.getActiveSession(tableId);
                log.warn("--- [MainController] OCCUPIED 상태-세션 불일치 복구 완료 (tableId: {}, sessionId: {}) ---",
                        tableId, recoverSessionId);
            } else {
                log.warn("--- [MainController] OCCUPIED 상태지만 활성 세션 없음 (tableId: {}) - 대시보드 기준으로 메뉴 진입 허용 ---",
                        tableId);
            }
        }

        if (activeSession != null) {
            session.setAttribute("partySize", activeSession.getInitialGuestCnt());
            session.setAttribute("packageId", activeSession.getPackageId());
            long checkInMillis = activeSession.getCheckInTime()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
            session.setAttribute("sessionStartTime", checkInMillis);
        }

        log.info("키오스크 -> 메뉴 진입 검증 완료(대시보드 기준) | tableNumber: {}, partySize: {}, packageId: {}",
                session.getAttribute("tableNumber"),
                session.getAttribute("partySize"),
                session.getAttribute("packageId"));

        return "redirect:/kiosk/drinks";
    }
    /*
     * 작성자 : 서주연
     * 기능 : 비밀번호 찾기 페이지 조회
     * 날짜 : 2026-04-08
     */


    @GetMapping("/admin/find_pw")
    public String findPwPage() {
        return "login/find_pw";
    }
    /*
     * 작성자 : 서주연
     * 기능 : AI Service
     * 날짜 : 2026-04-29
     */

    @GetMapping("/kiosk/ai")
    public void aiService() {
    }
}
