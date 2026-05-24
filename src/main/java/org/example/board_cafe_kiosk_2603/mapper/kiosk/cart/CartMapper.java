package org.example.board_cafe_kiosk_2603.mapper.kiosk.cart;

import org.apache.ibatis.annotations.Mapper;
import org.example.board_cafe_kiosk_2603.domain.kiosk.cart.Cart;


@Mapper

/*
 * 작성자 : 김민기
 * 기능 : 장바구니 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface CartMapper {

    /*
     * 작성자 : 김민기
     * 기능 : 테이블 번호로 테이블 ID 조회
     * 날짜 : 2026-03-27
     */
    Integer findCafeTableIdByTableNumber(int tableNumber);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    Cart findByTableId(int tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-27
     */
    void insert(Cart cart);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블 기준 장바구니 삭제
     * 날짜 : 2026-03-27
     */
    void deleteByTableId(int tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 갱신 시각 수정
     * 날짜 : 2026-03-27
     */
    void updateTimestamp(int cartId);
}

