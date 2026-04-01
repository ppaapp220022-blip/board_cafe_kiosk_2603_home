package org.example.board_cafe_kiosk_2603.controller.admin.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.kiosk.order.OrdersDTO;
import org.example.board_cafe_kiosk_2603.service.kiosk.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    /**
     * [PATCH] 주문 상태 변경 (관리자 대시보드용)
     * 경로: /admin/orders/{orderId}/status
     */
    @PatchMapping("/{orderId}/status")
// ResponseEntity<OrdersDTO> -> ResponseEntity<?> 로 변경
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable("orderId") Integer orderId,
            @RequestBody Map<String, String> request) {

        String nextStatus = request.get("status");

        try {
            orderService.updateOrderItemStatus(orderId, nextStatus);

            // 이제 Map.of가 정상적으로 수용됩니다.
            return ResponseEntity.ok(Map.of("message", "상태가 변경되었습니다."));

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "서버 오류 발생"));
        }
    }
}
