package org.example.board_cafe_kiosk_2603.controller.kiosk.cart;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.kiosk.cart.CartDTO;
import org.example.board_cafe_kiosk_2603.dto.kiosk.cart.CartItemDTO;
import org.example.board_cafe_kiosk_2603.service.kiosk.cart.CartService;
import org.example.board_cafe_kiosk_2603.service.kiosk.tableSession.TableSessionKioskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/*
 * 작성자 : 김민기
 * 기능 : 장바구니 페이지 + REST API.
 * 날짜 : 2026-03-27
 */

@Log4j2
@Controller
@RequestMapping("/kiosk/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService     cartService;
    private final TableSessionKioskService tableSessionKioskService;
    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 페이지 조회
     * 날짜 : 2026-03-27
     */


    @GetMapping
    public String cartPage(HttpSession session, Model model) {
        Integer tableNumber = tableNumber(session);
        if (tableNumber == null) return "redirect:/kiosk/session/start";
        tableSessionKioskService.buildCartModel(model, tableNumber, session);
        return "kiosk/cart";
    }
    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 조회
     * 날짜 : 2026-03-27
     */


    @GetMapping("/items")
    @ResponseBody
    public ResponseEntity<CartDTO> getCart(HttpSession session) {
        Integer tableNumber = tableNumber(session);
        if (tableNumber == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(cartService.getCart(tableNumber));
    }

    /*
     * 작성자 : 김민기
     * 기능 : addToCart 메서드
     * 날짜 : 2026-03-27
     */

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody CartItemDTO item,
                                             HttpSession session) {
        try {
            Integer tableNumber = tableNumber(session);
            if (tableNumber == null) {
                log.warn("--- [CartController] tableNumber가 null입니다 ---");
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "테이블 정보 없음"));
            }
            CartDTO result = cartService.addItem(tableNumber, item);
            if (!result.isSuccess()) {
                log.warn("장바구니 추가 실패 - {}", result.getMessage());
                return ResponseEntity.badRequest().body(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("장바구니 추가 중 오류 발생", e);
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /*
     * 작성자 : 김민기
     * 기능 : updateCart 메서드
     * 날짜 : 2026-03-27
     */

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<CartDTO> updateCart(@RequestBody CartItemDTO item,
                                              HttpSession session) {
        Integer tableNumber = tableNumber(session);
        if (tableNumber == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(cartService.updateItem(tableNumber, item));
    }

    /*
     * 작성자 : 김민기
     * 기능 : clearCart 메서드
     * 날짜 : 2026-03-27
     */

    @DeleteMapping("/clear")
    @ResponseBody
    public ResponseEntity<CartDTO> clearCart(HttpSession session) {
        Integer tableNumber = tableNumber(session);
        if (tableNumber == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(cartService.clearCart(tableNumber));
    }
    /*
     * 작성자 : 김민기
     * 기능 : 세션에서 테이블 번호 조회
     * 날짜 : 2026-03-27
     */

    private Integer tableNumber(HttpSession session) {
        Object raw = session.getAttribute("tableNumber");
        if (raw == null) {
            log.warn("--- [CartController] session에 tableNumber 없음 ---");
            return null;
        }
        if (raw instanceof Integer) {
            return (Integer) raw;
        }
        // String으로 저장된 이전 세션 방어 처리
        try {
            int parsed = Integer.parseInt(raw.toString());
            log.warn("--- [CartController] tableNumber가 String으로 저장됨 → 변환 처리: {} ---", parsed);
            // 이후 요청에서 다시 변환하지 않도록 Integer로 재저장
            session.setAttribute("tableNumber", parsed);
            return parsed;
        } catch (NumberFormatException e) {
            log.error("--- [CartController] tableNumber 변환 실패 | raw: {} ---", raw);
            return null;
        }
    }

}
