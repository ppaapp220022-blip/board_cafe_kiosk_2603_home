package org.example.board_cafe_kiosk_2603.repository.admin;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.macro.MacroMessage;
import org.example.board_cafe_kiosk_2603.mapper.admin.macro.MacroMessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/*
 * 작성자 : 강수연
 * 기능 : MacroMassageRepository 테스트
 * 날짜 : 2026-03-25
 */

@Log4j2
@SpringBootTest
class MacroMassageRepositoryTest {
    @Autowired
    private MacroMessageMapper macroMessageMapper;

    /*
     * 작성자 : 강수연
     * 기능 : findAllActive 메서드
     * 날짜 : 2026-03-25
     */

    @Test
    void findAllActive() {
        List<MacroMessage> macroMassages = macroMessageMapper.findAllActive();
        macroMassages.forEach(macroMassage -> log.info(macroMassage));
    }

}
