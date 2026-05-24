package org.example.board_cafe_kiosk_2603.mapper.kiosk;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.kiosk.cart.Cart;
import org.example.board_cafe_kiosk_2603.domain.kiosk.cart.CartItem;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.cart.CartItemMapper;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.cart.CartMapper;
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
 * 기능 : CartItemMapper 테스트
 * 날짜 : 2026-03-27
 */

@Log4j2
@SpringBootTest
@Transactional
class CartItemMapperTest {

    @Autowired private CartMapper cartMapper;
    @Autowired private CartItemMapper cartItemMapper;

    private int cartId;
    private int menuId;

    /*
     * 작성자 : 김민기
     * 기능 : setUp 메서드
     * 날짜 : 2026-03-27
     */

    @BeforeEach
    void setUp() {
        Integer tableId = cartMapper.findCafeTableIdByTableNumber(1);
        cartMapper.deleteByTableId(tableId);
        Cart cart = Cart.builder().tableId(tableId).build();
        cartMapper.insert(cart);
        cartId = cart.getId();

        menuId = 1;
    }

    /*
     * 작성자 : 김민기
     * 기능 : findMenuIdByNameAndPrice_success 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    @Disabled("더미 메뉴 이름/가격 변경에 따라 조회 성공 조건이 로컬 데이터와 쉽게 어긋납니다.")
    void findMenuIdByNameAndPrice_success() {
        Integer foundMenuId = cartItemMapper.findMenuIdByNameAndPrice("아메리카노", 3000);
        assertThat(foundMenuId).isNotNull().isPositive();
    }

    /*
     * 작성자 : 김민기
     * 기능 : findMenuIdByNameAndPrice_notFound 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    @Disabled("더미 메뉴 이름/가격 변경에 따라 조회 기준이 자주 바뀌므로 고정 문자열 의존 테스트를 보류합니다.")
    void findMenuIdByNameAndPrice_notFound() {
        Integer id = cartItemMapper.findMenuIdByNameAndPrice("없는메뉴", 99999);
        assertThat(id).isNull();
    }

    /*
     * 작성자 : 김민기
     * 기능 : insert_and_findByCartId 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void insert_and_findByCartId() {
        CartItem item = CartItem.builder()
                .cartId(cartId).menuId(menuId).quantity(2).build();
        cartItemMapper.insert(item);
        assertThat(item.getId()).isPositive();

        List<CartItem> items = cartItemMapper.findByCartId(cartId);
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getMenuId()).isEqualTo(menuId);
        assertThat(items.get(0).getQuantity()).isEqualTo(2);
        assertThat(items.get(0).getMenuName()).isNotBlank();
        assertThat(items.get(0).getMenuPrice()).isPositive();
    }

    /*
     * 작성자 : 김민기
     * 기능 : findByCartId_empty 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findByCartId_empty() {
        List<CartItem> items = cartItemMapper.findByCartId(cartId);
        assertThat(items).isEmpty();
    }

    /*
     * 작성자 : 김민기
     * 기능 : findByCartIdAndMenuId_success 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findByCartIdAndMenuId_success() {
        cartItemMapper.insert(CartItem.builder().cartId(cartId).menuId(menuId).quantity(1).build());

        CartItem found = cartItemMapper.findByCartIdAndMenuId(cartId, menuId);
        assertThat(found).isNotNull();
        assertThat(found.getQuantity()).isEqualTo(1);
    }

    /*
     * 작성자 : 김민기
     * 기능 : findByCartIdAndMenuId_notFound 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findByCartIdAndMenuId_notFound() {
        CartItem found = cartItemMapper.findByCartIdAndMenuId(cartId, 99999);
        assertThat(found).isNull();
    }

    /*
     * 작성자 : 김민기
     * 기능 : updateQuantity 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void updateQuantity() {
        cartItemMapper.insert(CartItem.builder().cartId(cartId).menuId(menuId).quantity(1).build());
        cartItemMapper.updateQuantity(cartId, menuId, 5);

        CartItem found = cartItemMapper.findByCartIdAndMenuId(cartId, menuId);
        assertThat(found.getQuantity()).isEqualTo(5);
    }

    /*
     * 작성자 : 김민기
     * 기능 : deleteByCartIdAndMenuId 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void deleteByCartIdAndMenuId() {
        cartItemMapper.insert(CartItem.builder().cartId(cartId).menuId(menuId).quantity(1).build());
        cartItemMapper.deleteByCartIdAndMenuId(cartId, menuId);

        assertThat(cartItemMapper.findByCartIdAndMenuId(cartId, menuId)).isNull();
    }

    /*
     * 작성자 : 김민기
     * 기능 : deleteAllByCartId 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void deleteAllByCartId() {
        cartItemMapper.insert(CartItem.builder().cartId(cartId).menuId(menuId).quantity(1).build());
        cartItemMapper.deleteAllByCartId(cartId);

        assertThat(cartItemMapper.findByCartId(cartId)).isEmpty();
    }
}
