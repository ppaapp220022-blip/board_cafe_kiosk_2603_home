package org.example.board_cafe_kiosk_2603.domain.admin.policy;

import lombok.*;

import java.time.LocalDateTime;

/*
 * 작성자 : 서민성
 * 기능 : Policy 클래스
 * 날짜 : 2026-04-03
 */

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Policy {
    private int           id;
    private String        name;
    private String        type;
    private Integer       durationMinutes;
    private int           basePrice;
    private Double        extraPricePerMin;
    private boolean       active;
    private LocalDateTime updatedAt;
}
