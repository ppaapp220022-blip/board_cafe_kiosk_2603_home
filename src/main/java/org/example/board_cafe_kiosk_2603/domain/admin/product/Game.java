package org.example.board_cafe_kiosk_2603.domain.admin.product;

import lombok.*;
/*
 * 작성자 : 서주연
 * 기능 : game 테이블과 1:1 매핑되는 도메인(VO) 클래스
 * 날짜 : 2026-03-27
 */

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private int id;

    /** FK → category.id (ON DELETE SET NULL), category.type = 'GAME' */
    private Integer categoryId;

    private String name;

    /** 최소 플레이 인원 */
    private Integer minPlayers;

    /** 최대 플레이 인원 */
    private Integer maxPlayers;

    /** 평균 플레이 시간 (분) */
    private Integer playTime;

    /** 활성 여부 (true: 대여 가능 / false: 비활성) */
    private boolean isActive;

    private String imageUrl;
}
