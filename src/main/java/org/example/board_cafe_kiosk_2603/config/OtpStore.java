package org.example.board_cafe_kiosk_2603.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * 작성자 : 서주연
 * 기능 : Otp 저장소 컴포넌트
 * 날짜 : 2026-04-08
 */

@Log4j2
@Component
public class OtpStore {

    private record OtpEntry(String code, LocalDateTime expiredAt) {
    }

    // 저장소 -> 이메일을 Key로, OTP 정보를 Value로 저장
    // ConcurrentHashMap을 사용하여 여러 사용자가 동시에 접근해도 안전함.
    private final Map<String, OtpEntry> store = new ConcurrentHashMap<>();
    public void save(String email, String code) {
        log.info("--- [OTP 생성] Email: {}, ExpiredAt: {} ---", email, LocalDateTime.now().plusMinutes(3));
        store.put(email, new OtpEntry(code, LocalDateTime.now().plusMinutes(3)));
    }
    public boolean verify(String email, String code) {
        OtpEntry entry = store.get(email);

        // 해당 이메일로 발급된 OTP가 없는 경우
        if (entry == null) {
            log.warn("--- [OTP 검증 실패] 발급 기록 없음 - Email: {} ---", email);
            return false;
        }

        // 만료 시간 체크
        if (LocalDateTime.now().isAfter(entry.expiredAt())) {
            log.warn("--- [OTP 검증 실패] 시간 만료 - Email: {}, 만료시각: {} ---", email, entry.expiredAt());
            store.remove(email);  // 만료된 데이터는 즉시 삭제
            return false;
        }

        // 번호 일치 여부 확인
        if (!entry.code().equals(code)) {
            log.warn("--- [OTP 검증 실패] 번호 불일치 - Email: {} ---", email);
            return false;
        }

        // 한 번 성공한 번호로 다시 인증할 수 없도록 메모리에서 제거
        log.info("--- [OTP 검증 성공] 인증 완료 및 삭제 - Email: {} ---", email);
        store.remove(email); // 검증 성공 시 즉시 삭제
        return true;
    }
}
