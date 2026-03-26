package org.example.board_cafe_kiosk_2603.domain.admin;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CafeTable {
    /* 시스템 PK */
    private Integer id;

    /* 실물 테이블 번호 */
    private Integer tableNumber;

    /* 태블릿 접속 비밀번호 */
    private String password;

    /**
     * 테이블 상태값 (String 처리)
     * EMPTY: 빈 자리 / OCCUPIED: 이용 중 / CLEANING: 정리 필요
     */
    private String status;

    /* 태블릿 로그인 유지용 토큰 */
    private String accessToken;

    /* 손님 입장 시각 */
    private LocalDateTime checkInTime;

    /* 입장 후 현재까지의 이용 시간(분) 계산 */
    public long getElapsedMinutes() {
        if (checkInTime == null) return 0;
        return java.time.Duration.between(checkInTime, LocalDateTime.now()).toMinutes();
    }
}
