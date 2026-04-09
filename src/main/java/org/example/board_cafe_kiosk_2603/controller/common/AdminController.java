package org.example.board_cafe_kiosk_2603.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.service.admin.point.PointService;
import org.example.board_cafe_kiosk_2603.service.admin.macro.MacroMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Admin 관리자 페이지 컨트롤러
 * 모든 관리자 페이지의 라우팅을 담당합니다.
 * <p>
 * URL 패턴: /admin/*
 * 뷰 경로: templates/admin/*
 */
@Log4j2
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PointService pointService;

    // 포인트 관리 페이지 이동 로직
//    @GetMapping("/points")
//    public String pointManagement(Model model) {
//        // 화면 확인을 위한 더미 데이터 (추후 DB 연동)
//        List<Map<String, Object>> pointList = new ArrayList<>();
//        pointList.add(Map.of("phone", "010-1234-5678", "balance", 12500, "updatedAt", LocalDateTime.now()));
//        pointList.add(Map.of("phone", "010-9876-5432", "balance", 5000, "updatedAt", LocalDateTime.now()));
//        pointList.add(Map.of("phone", "010-2222-2222", "balance", 2222, "updatedAt", LocalDateTime.now()));
//
//        model.addAttribute("pointList", pointList);
//        model.addAttribute("totalCustomers", 2);
//        model.addAttribute("totalPoints", 17500);
//        model.addAttribute("avgPoints", 8750);
//
//        // 중요: 사이드바 하이라이트를 위해 'pointManagement' 전달
//        model.addAttribute("activePage", "pointManagement");
//
//        return "admin/point"; // templates/admin/point.html 호출
//        model.addAttribute("pointList",      pointService.getAllPoints());
//        model.addAttribute("totalCustomers", pointService.getTotalCustomers());
//        model.addAttribute("totalPoints",    pointService.getTotalPoints());
//        model.addAttribute("avgPoints",      pointService.getAvgPoints());
//        model.addAttribute("activePage",     "pointManagement");
//        return "admin/point";
//    }

    // 패키지 요금 정책
    @GetMapping("/package")
    public String addPackage(Model model) {
        log.info("--- AdminController addPackage post ---");

        // 중요: 사이드바 하이라이트를 위해 'pointManagement' 전달
        model.addAttribute("activePage", "packageManagement");
        return "admin/package";
    }
}
