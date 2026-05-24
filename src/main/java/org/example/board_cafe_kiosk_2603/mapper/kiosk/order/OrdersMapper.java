package org.example.board_cafe_kiosk_2603.mapper.kiosk.order;

import org.apache.ibatis.annotations.Mapper;
import org.example.board_cafe_kiosk_2603.domain.kiosk.order.OrderItem;
import org.example.board_cafe_kiosk_2603.domain.kiosk.order.Orders;

import java.util.List;


@Mapper

/*
 * 작성자 : 김민기
 * 기능 : 주문 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface OrdersMapper {

    /*
     * 작성자 : 김민기
     * 기능 : 주문 등록
     * 날짜 : 2026-03-27
     */
    void insertOrder(Orders order);

    /*
     * 작성자 : 김민기
     * 기능 : 주문 상품 등록
     * 날짜 : 2026-03-27
     */
    void insertOrderItem(OrderItem item);

    /*
     * 작성자 : 김민기
     * 기능 : 주문 상태 변경
     * 날짜 : 2026-03-27
     */
    void updateOrderStatus(Orders order);

    /*
     * 작성자 : 김민기
     * 기능 : 주문 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    Orders findByOrderId(int orderId);

    /*
     * 작성자 : 김민기
     * 기능 : 세션 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    List<Orders> findBySessionId(long sessionId);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블의 최신 주문 조회
     * 날짜 : 2026-03-27
     */
    Orders findLatestByTableId(int tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 상태 기준 조회
     * 날짜 : 2026-04-09
     */
    List<Orders> findByStatus(String status);

    /*
     * 작성자 : 김민기
     * 기능 : 주문별 상품 목록 조회
     * 날짜 : 2026-03-27
     */
    List<OrderItem> findItemsByOrderId(int orderId);

}

