package org.example.board_cafe_kiosk_2603.domain.admin;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    private int id;
    private String phone;
    private int balance;
    private LocalDateTime updatedAt;
}
