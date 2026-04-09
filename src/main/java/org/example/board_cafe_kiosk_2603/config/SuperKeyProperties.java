package org.example.board_cafe_kiosk_2603.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "portfolio.super-key")
@Getter
@Setter
public class SuperKeyProperties {
    /*
    포트폴리오 시연용
    - 데이터베이스에 등록되어 있는 ID, PW, email 인증 통과 시 SUPER_OTP를 통해 즉시 로그인 가능
     */

    /*
     * 포트폴리오 시연용 슈퍼키 설정
     *
     * - DB에 등록된 ID/PW/email 인증을 통과한 상태에서
     *   OTP 입력 시 실제 발송된 OTP와 달라도 superOtp 값이면 인증 통과
     * - 모든 ADMIN 계정에 적용
     * - 값은 application-portfolio.properties 에서 주입
     */

    // SUPER_OTP
    // 실제 발송된 OTP와 달라도 입력값이 SUPER_OTP 값이면 인증 통과 (모든 계정 적용)

    private String id;   // 슈퍼 계정 loginId (DB에 실제 존재해야 함)
    private String otp;  // 슈퍼패스 OTP
    private String tempPasswd;  // 슈퍼패스 사용 시 임시 비밀번호

    /**
     * 슈퍼패스 OTP 여부 확인
     */
    public boolean isSuperOtp(String inputOtp) {
        return otp != null && otp.equals(inputOtp);
    }
}


/**
 * <<구현 끝나고 삭제할 것 구현>>
 * setter
 * 빈을 생성한 후, 설정 파일에 적힌 값을 가져와 setId()메서드를 호출하여 값을 채움
 * setter가 없으면 외부 설정값이 필드에 할당되지 않고 null 상태로 남음
 * <p>
 * getter
 * 주입된 값을 서비스나 컨트롤러 등 다른 로직에 사용하려면 외부에서 읽을 수 있어야 함.
 * properties.getId()와 같이 호출하기 위해 getter 필요.
 */
