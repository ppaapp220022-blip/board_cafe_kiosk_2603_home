package org.example.board_cafe_kiosk_2603.mapper.admin.macro;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.macro.MacroMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class MacroMessageMapperTest {
    @Autowired
    private MacroMessageMapper macroMessageMapper;

    @Test
    public void findAllActiveTest() {
        List<MacroMessage> activeMessages = macroMessageMapper.findAllActive();

        log.info("=== 활성 매크로 메시지 목록 (총 {}건) ===", activeMessages.size());
        for (MacroMessage msg : activeMessages) {
            log.info("ID: {} | 방향: {} | 내용: {} | 활성화여부: {}",
                    msg.getId(), msg.getDirection(), msg.getMessageText(), msg.isActive());
        }
    }

    @Test
    public void findByIdTest() {
        // 실제 DB에 존재하는 ID를 사용하세요 (예: 1)
        Integer targetId = 1;
        MacroMessage message = macroMessageMapper.findById(targetId);

        if (message != null) {
            log.info("=== 매크로 상세 조회 결과 (ID: {}) ===", targetId);
            log.info("내용: {} | 전송방향: {}", message.getMessageText(), message.getDirection());

        } else {
            log.warn("ID {}번에 해당하는 매크로 메시지가 DB에 없습니다.", targetId);
        }
    }

    @Test
    public void checkOrderTest() {
        List<MacroMessage> activeMessages = macroMessageMapper.findAllActive();

        if (activeMessages.size() >= 2) {
            log.info("첫 번째 메시지 방향: {}, ID: {}", activeMessages.get(0).getDirection(), activeMessages.get(0).getId());
            log.info("두 번째 메시지 방향: {}, ID: {}", activeMessages.get(1).getDirection(), activeMessages.get(1).getId());
        }
    }
}