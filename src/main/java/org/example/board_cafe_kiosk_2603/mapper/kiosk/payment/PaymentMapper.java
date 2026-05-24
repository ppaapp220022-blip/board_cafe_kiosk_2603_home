package org.example.board_cafe_kiosk_2603.mapper.kiosk.payment;

import org.apache.ibatis.annotations.Mapper;
import org.example.board_cafe_kiosk_2603.domain.kiosk.payment.Payment;

@Mapper

/*
 * 작성자 : 김민기
 * 기능 : 결제 데이터 접근 인터페이스
 * 날짜 : 2026-04-06
 */
public interface PaymentMapper {

    /*
     * 작성자 : 김민기
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-06
     */
    void insert(Payment payment);

    /*
     * 작성자 : 김민기
     * 기능 : 결제 키로 결제 조회
     * 날짜 : 2026-04-06
     */
    Payment findByPaymentKey(String paymentKey);

    /*
     * 작성자 : 김민기
     * 기능 : 주문 ID로 결제 조회
     * 날짜 : 2026-04-06
     */
    Payment findByOrderIdToss(String orderIdToss);

    /*
     * 작성자 : 김민기
     * 기능 : 세션 ID 기준 조회
     * 날짜 : 2026-04-06
     */
    Payment findBySessionId(long sessionId);

    /*
     * 작성자 : 김민기
     * 기능 : 상태 변경
     * 날짜 : 2026-04-06
     */
    void updateStatus(Payment payment);

    /*
     * 작성자 : 김민기
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-04-06
     */
    Payment findById(int id);
}

