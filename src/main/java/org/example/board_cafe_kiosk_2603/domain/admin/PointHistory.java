package org.example.board_cafe_kiosk_2603.domain.admin;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory {
    private long id;
    private int pointId;
    private Integer orderId;     // nullable (주문 없이 수동 조정 가능)
    private String type;         // EARN | USE
    private int amount;
    private int balanceAfter;
    private LocalDateTime createdAt;
}
