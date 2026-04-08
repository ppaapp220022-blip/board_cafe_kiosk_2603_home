package org.example.board_cafe_kiosk_2603.controller.common;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.common.kioskItem;
import org.example.board_cafe_kiosk_2603.service.admin.cafeTable.CafeTableService;
import org.example.board_cafe_kiosk_2603.service.admin.product.GameService;
import org.example.board_cafe_kiosk_2603.service.admin.product.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 키오스크 통합 컨트롤러
 * <p>
 * [로그인]
 * GET  /kiosk/login            → 로그인 페이지
 * POST /kiosk/login-process    → 로그인 처리 (테이블번호 + 비밀번호)
 * GET  /kiosk/logout           → 로그아웃
 * <p>
 * [진입 화면]
 * GET /kiosk/screensaver       → 스크린세이버
 * GET /kiosk/headcount         → 인원수 선택
 * GET /kiosk/phone_login       → 전화번호 입력
 * <p>
 * [메뉴 화면]
 * GET /kiosk/games             → 게임 목록
 * GET /kiosk/drinks            → 음료 목록
 * GET /kiosk/food              → 음식 목록
 * GET /kiosk/members           → 추가인원 목록
 */
@Log4j2
@Controller
@RequestMapping("/kiosk")
@RequiredArgsConstructor
public class kioskController {

    private final GameService gameService;
    private final MenuService menuService;

    // ===========================================================
    // 진입 화면
    // ===========================================================

    // 진입 화면 GET만 담당
    @GetMapping("/screensaver")
    public String screensaver(HttpSession session, Model model) {
        model.addAttribute("tableNumber", session.getAttribute("tableNumber"));
        log.info("스크린세이버 접근 - 테이블: {}", session.getAttribute("tableNumber"));
        return "kiosk/screensaver";
    }

    @GetMapping("/headcount")
    public String headcount(HttpSession session, Model model) {
        model.addAttribute("tableNumber", session.getAttribute("tableNumber"));
        log.info("인원수 선택 화면 - 테이블: {}", session.getAttribute("tableNumber"));
        return "kiosk/headcount";
    }

    @GetMapping("/phone_login")
    public String phoneLogin(HttpSession session, Model model) {
        model.addAttribute("tableNumber", session.getAttribute("tableNumber"));
        model.addAttribute("partySize", session.getAttribute("partySize"));
        log.info("전화번호 입력 화면 - 테이블: {}", session.getAttribute("tableNumber"));
        return "kiosk/phone_login";
    }

    // ===========================================================
    // 세션 시작 (로그인 성공 후 → 인원수 입력 화면)
    // ===========================================================
    @GetMapping("/session/start")
    public String sessionStart(HttpSession session, Model model) {
        log.info("--- [KioskController] 인원수 입력 화면 진입 | tableNumber: {} ---",
                session.getAttribute("tableNumber"));
        model.addAttribute("tableNumber", session.getAttribute("tableNumber"));
        return "kiosk/screensaver";
    }

    // ===========================================================
    // 메뉴 화면 - 실제 DB 데이터
    // ===========================================================

    // 게임 목록 조회
    @GetMapping("/games")
    public String games(HttpSession session, Model model) {
        initCart(session);

        // 활성화된 게임만 가져와서 키오스크용 DTO(kioskItem)로 변환
        List<kioskItem> items = gameService.getByIsActive(true).stream()
                .map(g -> kioskItem.builder()
                        .name(g.getName())
                        .price(0)  // 게임은 대여료가 0원
                        .imageUrl(g.getImageUrl())
                        .stock(g.getGameItemCount())
                        .build())
                .toList();

        log.info("--- [메뉴 -> 게임] 조회 완료 - 데이터 개수: {}개 ---", items.size());

        buildMenuModel(model, session, "games", "게임", items);
        return "layout/kiosk_layout";
    }

    // 음료 목록 조회
    @GetMapping("/drinks")
    public String drinks(HttpSession session, Model model) {
        initCart(session);

        // 판매 가능(Available)하고 삭제되지 않은 음료만 필터링
        List<kioskItem> items = menuService.getByType("DRINK").stream()
                .filter(m -> m.isAvailable() && !m.isDeleted())
                .map(m -> {
                    log.info("변환 중인 메뉴: {}", m.getName()); // 로그를 찍어서 데이터가 도는지 확인
                    return kioskItem.builder()
                            .name(m.getName())
                            .price(m.getPrice())
                            .imageUrl(m.getImageUrl())
                            .stock(-1)  // 음식/음료는 무제한 재고로 표시
                            .build();
                })
                .toList();

        log.info("--- [메뉴 -> 음료] 조회 완료 - 데이터 개수: {}개 ---", items.size());

        buildMenuModel(model, session, "drinks", "음료", items);
        return "layout/kiosk_layout";
    }

    // 음식 목록 조회
    @GetMapping("/food")
    public String food(HttpSession session, Model model) {
        initCart(session);

        // 판매 가능(Available)하고 삭제되지 않은 음료만 필터링
        List<kioskItem> items = menuService.getByType("FOOD").stream()
                .filter(m -> m.isAvailable() && !m.isDeleted())
                .map(m -> kioskItem.builder()
                        .name(m.getName())
                        .price(m.getPrice())
                        .imageUrl(m.getImageUrl())
                        .stock(-1)  // 음식/음료는 무제한 재고로 표시
                        .build())
                .toList();

        log.info("--- [메뉴 -> 음식] 조회 완료 - 데이터 개수: {}개 ---", items.size());

        buildMenuModel(model, session, "food", "음식", items);
        return "layout/kiosk_layout";
    }

    // 추가인원 목록 조회
    @GetMapping("/members")
    public String members(HttpSession session, Model model) {
        initCart(session);

        // 판매 가능(Available)하고 삭제되지 않은 음료만 필터링
        List<kioskItem> items = menuService.getByType("GUEST").stream()
                .filter(m -> m.isAvailable() && !m.isDeleted())
                .map(m -> kioskItem.builder()
                        .name(m.getName())
                        .price(m.getPrice())
                        .imageUrl(m.getImageUrl())
                        .stock(-1)  // 무제한 재고로 표시
                        .build())
                .toList();
        buildMenuModel(model, session, "members", "추가인원", items);

        log.info("--- [메뉴 -> 추가인원] 조회 완료 - 데이터 개수: {}개 ---", items.size());
        return "layout/kiosk_layout";
    }

    // 화면에 필요한 공통 데이터(테이블 번호, 장바구니 수 등)을 모델에 담음
    private void buildMenuModel(Model model, HttpSession session,
                                String menuType, String title, List<kioskItem> items) {
        log.info("--- buildMenuModel ---");
        // 장바구니 개수 안전하게 처리
        Object cart = session.getAttribute("cart");
        int cartCount = cart instanceof List ? ((List<?>) cart).size() : 0;

        // 세션 유지 시간 정보 로그
        log.debug("세션 정보 - 시작시간: {}, 인원수: {}",
                session.getAttribute("sessionStartTime"), getPartySize(session));

        model.addAttribute("tableNumber", session.getAttribute("tableNumber"));
        model.addAttribute("partySize", getPartySize(session));
        model.addAttribute("currentMenu", menuType);  // 현재 탭 강조용
        model.addAttribute("pageTitle", title);  // 상단 제목용
        model.addAttribute("menuItems", items);  // 실제 메뉴 리스트
        model.addAttribute("cartCount", cartCount);  // 우측 하단 장바구니 숫자
        model.addAttribute("sessionStartTime", session.getAttribute("sessionStartTime"));
        model.addAttribute("durationMinutes", session.getAttribute("durationMinutes"));
    }


    // 인원수 기본값 처리
//    private int getPartySize(HttpSession session) {
//        Object val = session.getAttribute("partySize");
//        return val instanceof Integer ? (Integer) val : 2;
//    }

    // 인원수 미선택 시 다음 단계로 넘어가지 못하게 제어
    private int getPartySize(HttpSession session) {
        Object val = session.getAttribute("partySize");

        if (!(val instanceof Integer)) {
            log.error("[세션 에러] 인원수 데이터가 세션에 존재하지 않습니다.");
            throw new IllegalStateException("인원수 선택이 완료되지 않았습니다.");
        }
        return (Integer) val;
    }

    // 장바구니가 세션에 없으면 빈 리스트로 초기화 (null 방지)
    private void initCart(HttpSession session) {
        if (session.getAttribute("cart") == null) {
            log.info("--- HttpSession, 새로운 장바구니(Cart) 생성 ---");
            session.setAttribute("cart", new ArrayList<>());
        }
    }
}
