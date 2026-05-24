package org.example.board_cafe_kiosk_2603.mapper.kiosk;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.kiosk.cart.Cart;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.cart.CartMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

/*
 * 작성자 : 김민기
 * 기능 : CartMapper 테스트
 * 날짜 : 2026-03-27
 */

@Log4j2
@SpringBootTest
@Transactional
class CartMapperTest {

    @Autowired
    private CartMapper cartMapper;

    private Integer getTestTableId() {
        Integer tableId = cartMapper.findCafeTableIdByTableNumber(1);
        cartMapper.deleteByTableId(tableId);
        return tableId;
    }

    /*
     * 작성자 : 김민기
     * 기능 : findCafeTableIdByTableNumber_success 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findCafeTableIdByTableNumber_success() {
        Integer tableId = cartMapper.findCafeTableIdByTableNumber(1);
        assertThat(tableId).isNotNull().isPositive();
    }

    /*
     * 작성자 : 김민기
     * 기능 : findCafeTableIdByTableNumber_notFound 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findCafeTableIdByTableNumber_notFound() {
        Integer tableId = cartMapper.findCafeTableIdByTableNumber(999);
        assertThat(tableId).isNull();
    }

    /*
     * 작성자 : 김민기
     * 기능 : insert_and_findByTableId 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void insert_and_findByTableId() {
        Integer tableId = getTestTableId();
        assertThat(tableId).isNotNull();

        Cart newCart = Cart.builder().tableId(tableId).build();
        cartMapper.insert(newCart);
        assertThat(newCart.getId()).isPositive();

        Cart found = cartMapper.findByTableId(tableId);
        assertThat(found).isNotNull();
        assertThat(found.getTableId()).isEqualTo(tableId);
    }

    /*
     * 작성자 : 김민기
     * 기능 : findByTableId_notFound 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findByTableId_notFound() {
        Cart found = cartMapper.findByTableId(99999);
        assertThat(found).isNull();
    }

    /*
     * 작성자 : 김민기
     * 기능 : updateTimestamp 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void updateTimestamp() {
        Integer tableId = getTestTableId();
        Cart cart = Cart.builder().tableId(tableId).build();
        cartMapper.insert(cart);

        assertThatCode(() -> cartMapper.updateTimestamp(cart.getId()))
                .doesNotThrowAnyException();
    }

    /*
     * 작성자 : 김민기
     * 기능 : deleteByTableId 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void deleteByTableId() {
        Integer tableId = getTestTableId();
        Cart cart = Cart.builder().tableId(tableId).build();
        cartMapper.insert(cart);

        cartMapper.deleteByTableId(tableId);

        Cart found = cartMapper.findByTableId(tableId);
        assertThat(found).isNull();
    }
}
