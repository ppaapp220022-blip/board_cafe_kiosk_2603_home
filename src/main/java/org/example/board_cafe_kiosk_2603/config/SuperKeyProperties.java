package org.example.board_cafe_kiosk_2603.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/*
 * 작성자 : 서주연
 * 기능 : 포트폴리오 시연용
 * 날짜 : 2026-04-08
 */

@Component
@ConfigurationProperties(prefix = "portfolio.super-key")
@Getter
@Setter
public class SuperKeyProperties {

    private String id;   // 슈퍼 계정 loginId (DB에 실제 존재해야 함)
    private String otp;  // 슈퍼패스 OTP
    private String tempPasswd;  // 슈퍼패스 사용 시 임시 비밀번호
    public boolean isSuperOtp(String inputOtp) {
        return otp != null && otp.equals(inputOtp);
    }
}
