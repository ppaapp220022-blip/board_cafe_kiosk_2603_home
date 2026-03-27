package org.example.board_cafe_kiosk_2603.controller.common;

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
    }

    @GetMapping("/kiosk/login")
    public String kioskPage() {
        log.info("키오스크 -> 로그인 ...");
        return "/login/kiosk_login";
    }

    @PostMapping("/kiosk/login")
    public String kioskLoginProcess(
            @RequestParam(required = false, defaultValue = "1") Integer tableNumber,
            @RequestParam(required = false) String password,
            HttpSession session) {
        log.info("키오스크 로그인 처리 - 테이블: {}", tableNumber);
        session.setAttribute("tableNumber", tableNumber);
        return "redirect:/kiosk/screensaver";
    }

    @PostMapping("/admin/login-process")
    public String adminLoginProcess() {
        log.info("관리자 로그인 처리 중... 대시보드로 이동합니다.");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/main-dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("activePage", "tableStatus");
        model.addAttribute("isAdmin", true);

        List<Map<String, Object>> dummyTables = new ArrayList<>();
        dummyTables.add(Map.of("tableNumber", 1, "status", "occupied", "guestCount", 2, "elapsedTime", "1h 10m"));
        dummyTables.add(Map.of("tableNumber", 2, "status", "waiting",  "guestCount", 4, "elapsedTime", "0h 15m"));
        dummyTables.add(Map.of("tableNumber", 3, "status", "empty"));
        model.addAttribute("tableList", dummyTables);

        return "admin/main_dashbord";
    }
}
