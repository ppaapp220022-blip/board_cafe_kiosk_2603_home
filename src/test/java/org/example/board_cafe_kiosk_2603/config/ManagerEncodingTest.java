package org.example.board_cafe_kiosk_2603.config;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.manager.Manager;
import org.example.board_cafe_kiosk_2603.domain.admin.manager.RoleType;
import org.example.board_cafe_kiosk_2603.mapper.admin.manager.ManagerMapper;
import org.example.board_cafe_kiosk_2603.mapper.admin.table.CafeTableMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 작성자 : 서주연
 * 기능 : ManagerEncoding 테스트
 * 날짜 : 2026-04-01
 */

@SpringBootTest
@Log4j2
@Transactional
class ManagerEncodingTest {

    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private CafeTableMapper cafeTableMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String uniqueLoginId() {
        return "e" + (System.currentTimeMillis() % 1_000_000);
    }
    @Test
    void passwordEncoderTest() {
        // 1. 준비
        String rawPassword = "12345";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String loginId = uniqueLoginId();

        // VO 생성 (생성자 또는 빌더 사용)
        Manager newManager = Manager.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name("테스트")
                .email(loginId + "@test.local")
                .role(RoleType.ADMIN)
                .isActive(true)
                .build();

        // 2. 실행
        managerMapper.insert(newManager);

        // 3. 검증 (Optional 처리 중요!)
        // .orElseThrow()를 사용하면 Optional 안의 Manager를 바로 꺼낼 수 있습니다.
        Manager savedManager = managerMapper.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

        log.info("입력 평문: {}", rawPassword);
        log.info("DB에 저장된 암호문: {}", savedManager.getPassword());

        // 4. 단언
        // BCrypt 암호화 결과가 평문과 다른지 확인
        Assertions.assertNotEquals(rawPassword, savedManager.getPassword());

        // BCrypt 전용 매칭 확인
        boolean isMatch = passwordEncoder.matches(rawPassword, savedManager.getPassword());
        Assertions.assertTrue(isMatch, "암호화된 비밀번호가 일치해야 합니다.");
    }
    @Test
    void getEncodedPasswordForManualUpdate() {
        String raw = "2222"; // 내가 쓰고 싶은 비번
        String encoded = passwordEncoder.encode(raw);

        // 로그에 찍힌 이 SQL 문을 그대로 복사해서 DB 툴에서 실행하세요!
        log.info("실행할 SQL 문장: UPDATE cafe_table SET password = '{}' WHERE table_number = 2;", encoded);
    }
}
