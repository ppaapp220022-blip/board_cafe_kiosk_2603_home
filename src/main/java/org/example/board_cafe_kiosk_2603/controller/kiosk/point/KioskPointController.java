package org.example.board_cafe_kiosk_2603.controller.kiosk.point;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.point.PointAdminDTO;
import org.example.board_cafe_kiosk_2603.dto.kiosk.point.PointKioskDTO;
import org.example.board_cafe_kiosk_2603.service.admin.point.PointService;
import org.springframework.web.bind.annotation.*;
/*
 * 작성자 : 김민기
 * 기능 : 키오스크 포인트 조회 REST API 컨트롤러.
 * 날짜 : 2026-03-27
 */

@Log4j2
@RestController
@RequestMapping("/kiosk/point")
@RequiredArgsConstructor
public class KioskPointController {

    private final PointService pointService;

    /*
     * 작성자 : 김민기
     * 기능 : lookupPoint 메서드
     * 날짜 : 2026-03-27
     */

    @GetMapping("/lookup")
    public PointKioskDTO lookupPoint(
            @RequestParam String phone,
            HttpSession session) {

        log.info("포인트 조회 - 전화번호: {}", phone);
        session.setAttribute("customerPhone", phone);

        PointAdminDTO point = pointService.getPointByPhone(phone);
        if (point != null) {
            log.info("기존 회원 - 잔액: {}P", point.getBalance());
            return PointKioskDTO.found(point.getBalance());
        }

        pointService.createAccount(phone);
        log.info("신규 회원 등록: {}", phone);
        return PointKioskDTO.notFound();
    }
}
