package org.example.board_cafe_kiosk_2603.service.kiosk.tableSession;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Map;

/*
 * 작성자 : 서민성
 * 기능 : 키오스크 테이블 세션 서비스 인터페이스
 * 날짜 : 2026-03-27
 */
public interface TableSessionKioskService {
    /*
     * 작성자 : 김민기
     * 기능 : 테이블 세션 생성
     * 날짜 : 2026-04-30
     */
    Long createSession(int tableId, int packageId, int initialGuestCnt);

    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 화면 모델 구성
     * 날짜 : 2026-04-30
     */
    void buildCartModel(Model model, int tableNumber, HttpSession session);

    /*
     * 작성자 : 김민기
     * 기능 : 결제 화면 모델 구성
     * 날짜 : 2026-04-30
     */
    void buildCheckoutModel(Model model, int tableNumber, HttpSession session);

    /*
     * 작성자 : 김민기
     * 기능 : 결제 메타 정보 구성
     * 날짜 : 2026-04-30
     */
    Map<String, Object> buildCheckoutMeta(Integer tableId);
}
