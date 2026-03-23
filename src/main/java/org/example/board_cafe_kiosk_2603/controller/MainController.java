package org.example.board_cafe_kiosk_2603.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "redirect:/common/login";
    }

    @GetMapping("/common/login")
    public String loginPage() {
        log.info("초기 페이지 접근 ...");
        return "common/login";
    }

    @GetMapping("/admin/login")
    public String adminKiosk() {
        log.info("관리자 -> 로그인 ...");
        return "/login/admin_login";
//        return "/admin/dashboard";
    }

    @GetMapping("/kiosk/login")
    public String kioskPage() {
        log.info("키오스크 -> 로그인 ...");
        return "/login/kiosk_login";
    }


    @PostMapping("/kiosk/login")
    public String kioskLoginProcess() {
        log.info("키오스크 로그인 처리 중 ...");
        return "redirect:/kiosk/screensaver";
    }
//
//    @GetMapping("/kiosk/screensaver")
//    public String kioskIdleScreen() {
//        log.info("키오스크 대기 화면 접근 ...");
//        return "kiosk/screensaver";
//    }
//
//    @GetMapping("/kiosk/headcount")
//    public String partySizePage() {
//        log.info("키오스크 -> 인원수 선택 화면");
//        return "kiosk/headcount";
//    }
//
//    @GetMapping("/kiosk/phone_login")
//    public String phoneLoginPage() {
//        log.info("키오스크 -> 휴대폰 번호 입력 화면");
//        return "kiosk/phone_login";
//    }
//
//    @GetMapping("/kiosk/main_menu")
//    public String mainMenuPage() {
//        log.info("키오스크 -> 메인 메뉴 화면 진입");
//        return "kiosk/main_menu";
//    }

    @PostMapping("/admin/login-process") // 에러 메시지에 나온 그 경로입니다!
    public String adminLoginProcess() {
        log.info("관리자 로그인 처리 중... 대시보드로 이동합니다.");
        return "redirect:/admin/dashboard"; // 로그인 성공 시 대시보드 GetMapping으로 리다이렉트
    }

    // 관리자 메인 대시보드 (테이블 현황)
    @GetMapping("/admin/main-dashboard")
    public String adminDashboard(Model model) {
        // 1. 사이드바 활성화 태그 (admin_layout.html의 th:classappend와 일치해야 함)
        model.addAttribute("activePage", "tableStatus");

        // 2. 관리자 여부 (true여야 Admin 뱃지가 나타남)
        model.addAttribute("isAdmin", true);

        // 3. 화면에 뿌려줄 임시 데이터 (DB 연동 전까지 더미 데이터 활용)
        List<Map<String, Object>> dummyTables = new ArrayList<>();
        // 예시 데이터 추가 (이 구조가 HTML의 th:text="${table.status}" 등과 매핑됨)
        dummyTables.add(Map.of("tableNumber", 1, "status", "occupied", "guestCount", 2, "elapsedTime", "1h 10m"));
        dummyTables.add(Map.of("tableNumber", 2, "status", "waiting", "guestCount", 4, "elapsedTime", "0h 15m"));
        dummyTables.add(Map.of("tableNumber", 3, "status", "empty"));

        model.addAttribute("tableList", dummyTables);

        return "admin/main_dashbord"; // templates/admin/main_dashbord.html 호출
    }


}
