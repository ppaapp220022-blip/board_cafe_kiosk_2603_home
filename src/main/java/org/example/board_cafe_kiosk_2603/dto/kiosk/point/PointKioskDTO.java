package org.example.board_cafe_kiosk_2603.dto.kiosk.point;

import lombok.Builder;
import lombok.Getter;

/*
 * 작성자 : 김민기
 * 기능 : PointKiosk 데이터 전달 객체
 * 날짜 : 2026-03-27
 */

@Getter
@Builder
public class PointKioskDTO {

    private final boolean exists;
    private final int     balance;

    /*
     * 작성자 : 김민기
     * 기능 : of 메서드
     * 날짜 : 2026-03-27
     */

    public static PointKioskDTO of(boolean exists, int balance) {
        return PointKioskDTO.builder()
                .exists(exists)
                .balance(balance)
                .build();
    }

    /*
     * 작성자 : 김민기
     * 기능 : found 메서드
     * 날짜 : 2026-03-27
     */

    public static PointKioskDTO found(int balance) {
        return PointKioskDTO.builder()
                .exists(true)
                .balance(balance)
                .build();
    }

    /*
     * 작성자 : 김민기
     * 기능 : notFound 메서드
     * 날짜 : 2026-03-27
     */

    public static PointKioskDTO notFound() {
        return PointKioskDTO.builder()
                .exists(false)
                .balance(0)
                .build();
    }
}
