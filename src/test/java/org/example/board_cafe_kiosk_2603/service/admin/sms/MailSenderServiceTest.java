package org.example.board_cafe_kiosk_2603.service.admin.sms;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;

/*
 * 작성자 : 서주연
 * 기능 : MailSenderService 테스트
 * 날짜 : 2026-04-08
 */

@Log4j2
@SpringBootTest
class MailSenderServiceTest {
    @Autowired
    private MailSenderService mailSenderService;

    @Value("${MAIL_TEST_TO:${SPRING_MAIL_USERNAME:}}")
    private String toEmail;

    @Test
    public void sendMailTest() throws Exception {
        // 6자리 난수 생성
        String verificationCode = mailSenderService.generateVerificationCode();

        if (toEmail == null || toEmail.isBlank()) {
            log.warn("MAIL_TEST_TO 또는 SPRING_MAIL_USERNAME 값이 없어 메일 발송 테스트를 건너뜁니다.");
            return;
        }

        log.info("테스트 시작 - 수신자: {}, 생성된 인증번호: {}", toEmail, verificationCode);

        // 메일 발송 메서드 호출
        mailSenderService.sendMailForAlarm(toEmail, verificationCode);

        log.info("--- 테스트 메일 발송 요청 완료 ---");
    }

}
