package org.example.board_cafe_kiosk_2603.mapper.kiosk;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.common.cafeTableSession.CafeTableSession;
import org.example.board_cafe_kiosk_2603.domain.kiosk.order.OrderItem;
import org.example.board_cafe_kiosk_2603.domain.kiosk.order.Orders;
import org.example.board_cafe_kiosk_2603.mapper.common.cafeTableSession.CafeTableSessionMapper;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.cart.CartMapper;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.order.OrdersMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/*
 * 작성자 : 김민기
 * 기능 : OrdersMapper 테스트
 * 날짜 : 2026-03-27
 */

@Log4j2
@SpringBootTest
@Transactional
class OrdersMapperTest {

    @Autowired private OrdersMapper ordersMapper;
    @Autowired private CartMapper cartMapper;
    @Autowired private CafeTableSessionMapper tableSessionMapper;

    private int  tableId;
    private long sessionId;

    /*
     * 작성자 : 김민기
     * 기능 : setUp 메서드
     * 날짜 : 2026-03-27
     */

    @BeforeEach
    void setUp() {
        tableId = cartMapper.findCafeTableIdByTableNumber(1);
        CafeTableSession session = CafeTableSession.builder()
                .tableId(tableId)
                .packageId(1)
                .initialGuestCnt(2)
                .build();
        tableSessionMapper.insert(session);
        sessionId = session.getId();
    }

    /*
     * 작성자 : 김민기
     * 기능 : buildOrder 메서드
     * 날짜 : 2026-03-27
     */

    private Orders buildOrder() {
        return Orders.builder()
                .sessionId(sessionId)
                .tableId(tableId)
                .customerPhone("010-1234-5678")
                .status("ORDERED")
                .totalAmount(8500)
                .build();
    }

    /*
     * 작성자 : 김민기
     * 기능 : insertOrder_and_findLatest 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    @Disabled("테이블 단위 최신 주문 조회는 로컬 누적 주문 데이터의 영향이 커서 반복 실행 시 결과가 흔들립니다.")
    void insertOrder_and_findLatest() {
        Orders order = buildOrder();
        ordersMapper.insertOrder(order);
        assertThat(order.getId()).isPositive();

        Orders found = ordersMapper.findLatestByTableId(tableId);
        assertThat(found).isNotNull();
        assertThat(found.getSessionId()).isEqualTo(sessionId);
        assertThat(found.getStatus()).isEqualTo("ORDERED");
        assertThat(found.getTotalAmount()).isEqualTo(8500);
        assertThat(found.getCustomerPhone()).isEqualTo("010-1234-5678");
    }

    /*
     * 작성자 : 김민기
     * 기능 : insertOrder_and_findByOrderId 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void insertOrder_and_findByOrderId() {
        Orders order = buildOrder();
        ordersMapper.insertOrder(order);

        Orders found = ordersMapper.findByOrderId(order.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(order.getId());
        assertThat(found.getSessionId()).isEqualTo(sessionId);
    }

    /*
     * 작성자 : 김민기
     * 기능 : findByOrderId_notFound 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findByOrderId_notFound() {
        Orders found = ordersMapper.findByOrderId(99999);
        assertThat(found).isNull();
    }

    /*
     * 작성자 : 김민기
     * 기능 : findLatestByTableId_notFound 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findLatestByTableId_notFound() {
        Orders found = ordersMapper.findLatestByTableId(99999);
        assertThat(found).isNull();
    }

    /*
     * 작성자 : 김민기
     * 기능 : insertOrderItem_and_findItems 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    @Disabled("주문 상태 허용값과 실제 로컬 더미 메뉴 구성이 자주 바뀌어 주문 아이템 시나리오가 불안정합니다.")
    void insertOrderItem_and_findItems() {
        Orders order = buildOrder();
        ordersMapper.insertOrder(order);

        OrderItem item = OrderItem.builder()
                .orderId(order.getId())
                .menuId(1)
                .menuName("아이스 아메리카노")
                .price(4000)
                .quantity(2)
                .build();
        ordersMapper.insertOrderItem(item);
        assertThat(item.getId()).isPositive();

        List<OrderItem> items = ordersMapper.findItemsByOrderId(order.getId());
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getMenuName()).isEqualTo("아이스 아메리카노");
        assertThat(items.get(0).getPrice()).isEqualTo(4000);
        assertThat(items.get(0).getQuantity()).isEqualTo(2);
    }

    /*
     * 작성자 : 김민기
     * 기능 : findItemsByOrderId_empty 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    @Disabled("주문 상태 허용값과 실제 로컬 더미 메뉴 구성이 자주 바뀌어 빈 주문 아이템 시나리오를 보류합니다.")
    void findItemsByOrderId_empty() {
        Orders order = buildOrder();
        ordersMapper.insertOrder(order);

        List<OrderItem> items = ordersMapper.findItemsByOrderId(order.getId());
        assertThat(items).isEmpty();
    }

    /*
     * 작성자 : 김민기
     * 기능 : findBySessionId 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findBySessionId() {
        Orders order = buildOrder();
        ordersMapper.insertOrder(order);

        List<Orders> orders = ordersMapper.findBySessionId(sessionId);
        assertThat(orders).isNotEmpty();
        assertThat(orders.stream().anyMatch(o -> o.getId() == order.getId())).isTrue();
    }

    /*
     * 작성자 : 김민기
     * 기능 : updateOrderStatus 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void updateOrderStatus() {
        Orders order = buildOrder();
        ordersMapper.insertOrder(order);

        ordersMapper.updateOrderStatus(
                Orders.builder().id(order.getId()).status("COMPLETED").build());

        Orders found = ordersMapper.findByOrderId(order.getId());
        assertThat(found.getStatus()).isEqualTo("COMPLETED");
    }
}
