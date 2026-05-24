package org.example.board_cafe_kiosk_2603.service.kiosk;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.service.kiosk.tableSession.TableSessionKioskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * 작성자 : 서민성
 * 기능 : CafeTableSessionMapperKioskService 테스트
 * 날짜 : 2026-03-27
 */

@Log4j2
@SpringBootTest
class CafeTableSessionMapperKioskServiceTest {

    @Autowired
    private TableSessionKioskService tableSessionKioskService;

    /*
     * 작성자 : 서민성
     * 기능 : createSessionTest 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void createSessionTest() {
        tableSessionKioskService.createSession(1, 1, 2);
        log.info("sessionTest...");
    }

}
