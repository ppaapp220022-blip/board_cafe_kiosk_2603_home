package org.example.board_cafe_kiosk_2603.service.admin;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.common.cafeTableSession.CafeTableSession;
import org.example.board_cafe_kiosk_2603.mapper.common.cafeTableSession.CafeTableSessionMapper;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.cart.CartMapper;
import org.example.board_cafe_kiosk_2603.service.admin.cafeTable.TableSessionAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/*
 * 작성자 : 서민성
 * 기능 : CafeTableSessionMapperAdminService 테스트
 * 날짜 : 2026-03-27
 */

@Log4j2
@SpringBootTest
@Transactional
class CafeTableSessionMapperAdminServiceTest {
    @Autowired
    private TableSessionAdminService tableSessionAdminService;
    @Autowired
    private CafeTableSessionMapper cafeTableSessionMapper;
    @Autowired
    private CartMapper cartMapper;

    /*
     * 작성자 : 서민성
     * 기능 : getActiveSessionTest 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void getActiveSessionTest() {
        CafeTableSession tableSession = tableSessionAdminService.getActiveSession(1);
        log.info("활성 세션 조회 결과... {}", tableSession);
    }

    /*
     * 작성자 : 서민성
     * 기능 : updateTotalAmountTest 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void updateTotalAmountTest() {
        // 활성 세션 조회 후 정산 금액 업데이트
        int tableId = cartMapper.findCafeTableIdByTableNumber(12);
        CafeTableSession session = CafeTableSession.builder()
                .tableId(tableId)
                .packageId(1)
                .initialGuestCnt(2)
                .build();
        cafeTableSessionMapper.insert(session);
        CafeTableSession tableSession = tableSessionAdminService.getActiveSession(tableId);

        tableSessionAdminService.updateTotalAmount(tableSession.getId(), 25000);
        log.info("정산 금액 업데이트... sessionId: {}, totalAmount: 25000", tableSession.getId());
    }


}
