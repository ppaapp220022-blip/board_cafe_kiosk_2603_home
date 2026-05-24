package org.example.board_cafe_kiosk_2603.mapper.kiosk.cart;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.kiosk.cart.CartItem;

import java.util.List;


@Mapper

/*
 * 작성자 : 김민기
 * 기능 : 장바구니 항목 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface CartItemMapper {

    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    List<CartItem> findByCartId(int cartId);

    /*
     * 작성자 : 김민기
     * 기능 : 장바구니와 메뉴 기준 항목 조회
     * 날짜 : 2026-03-27
     */
    CartItem findByCartIdAndMenuId(@Param("cartId") int cartId,
                                   @Param("menuId") int menuId);

    /*
     * 작성자 : 김민기
     * 기능 : 메뉴명과 가격으로 메뉴 ID 조회
     * 날짜 : 2026-03-27
     */
    Integer findMenuIdByNameAndPrice(@Param("name") String name,
                                     @Param("price") int price);

    /*
     * 작성자 : 김민기
     * 기능 : 게임 메뉴 여부 건수 조회
     * 날짜 : 2026-04-15
     */
    int countGameMenuByMenuId(@Param("menuId") int menuId);

    /*
     * 작성자 : 김민기
     * 기능 : 대여 가능한 게임 재고 건수 조회
     * 날짜 : 2026-04-15
     */
    int countAvailableGameStockByMenuId(@Param("menuId") int menuId);

    /*
     * 작성자 : 김민기
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-27
     */
    void insert(CartItem cartItem);

    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 수량 수정
     * 날짜 : 2026-03-27
     */
    void updateQuantity(@Param("cartId") int cartId,
                        @Param("menuId") int menuId,
                        @Param("quantity") int quantity);

    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 항목 삭제
     * 날짜 : 2026-03-27
     */
    void deleteByCartIdAndMenuId(@Param("cartId") int cartId,
                                 @Param("menuId") int menuId);

    /*
     * 작성자 : 김민기
     * 기능 : 장바구니 전체 항목 삭제
     * 날짜 : 2026-03-27
     */
    void deleteAllByCartId(int cartId);
}

