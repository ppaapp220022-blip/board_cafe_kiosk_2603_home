CREATE DATABASE IF NOT EXISTS `board_cafe_kiosk_2603`;
USE `board_cafe_kiosk_2603`;

--  보드게임 카페 키오스크 시스템 — 최종 고도화 스키마
--  수정 사항: table_session 추가(22번), GUEST 카테고리 확장, 세션 기반 관계 재설정
--
--  테이블 목록 (총 22개)
--  ┌─────┬─────────────────────┬────────────────────────────────────────────────┐
--  │  #  │ 테이블명              │ 역할 (비고)                                     │
--  ├─────┼─────────────────────┼────────────────────────────────────────────────┤
--  │  1  │ manager             │ 관리자·직원 계정 (ADMIN / STAFF)                  │
--  │  2  │ cafe_table          │ 물리적 테이블 (UUID access_token 추가)            │
--  │  3  │ customer            │ 전화번호 등록 고객 (포인트 대상)                     │
--  │  4  │ category            │ 메뉴·게임·인원(GUEST) 공통 카테고리                │
--  │  5  │ menu                │ 음식·음료 및 추가인원 상품                        │
--  │  6  │ game                │ 보드게임 종목                                   │
--  │  7  │ game_item           │ 보드게임 실물 재고 (박스 단위)                      │
--  │  8  │ cart                │ 테이블별 장바구니 헤더                            │
--  │  9  │ cart_item           │ 장바구니 담긴 메뉴 항목                            │
--  │ 10  │ macro_message       │ 1클릭 매크로 메시지                               │
--  │ 11  │ table_session       │ [NEW] 테이블 이용 히스토리 및 세션 관리 (핵심)        │
--  │ 12  │ orders              │ 주문 헤더 (session_id 외래키 추가)                │
--  │ 13  │ order_item          │ 주문 상세 항목 (메뉴·가격 스냅샷)                   │
--  │ 14  │ payment             │ 결제 헤더 (세션 단위 정산으로 변경)                  │
--  │ 15  │ toss_payment        │ Toss Payments API 전용 데이터                  │
--  │ 16  │ point               │ 전화번호 기반 포인트 계좌                          │
--  │ 17  │ point_history       │ 포인트 적립·사용 이력                             │
--  │ 18  │ table_message       │ 통합 메시지 로그                                 │
--  │ 19  │ item_sales_history  │ 일일 상품별 판매 통계                             │
--  │ 20  │ daily_sales_summary │ 매장 전체 일별 매출 요약                          │
--  │ 21  │ cafe_package        │ 패키지 요금 정책                                 │
--  │ 22  │ rental_log          │ 게임 대여 이력 (session_id 기반으로 변경)           │
--  └─────┴─────────────────────┴────────────────────────────────────────────────┘

-- 1. manager
CREATE TABLE `manager` (
                           `id`         INT                    NOT NULL AUTO_INCREMENT COMMENT '관리자/직원 고유 번호 (PK)',
                           `login_id`   VARCHAR(50)            NOT NULL COMMENT '로그인 아이디 (중복 불가)',
                           `password`   VARCHAR(255)           NOT NULL COMMENT 'BCrypt 암호화 비밀번호',
                           `name`       VARCHAR(30)            NOT NULL COMMENT '실명',
                           `role`       ENUM ('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF' COMMENT '권한: ADMIN(사장), STAFF(직원)',
                           `is_active`  BOOLEAN                NOT NULL DEFAULT TRUE COMMENT '활성 상태 (FALSE=비활성)',
                           `created_at` TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '계정 생성 일시',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uq_manager_login_id` (`login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='관리자·직원 계정.';

-- 2. cafe_table
-- 수정사항: 로그인 유지를 위한 access_token(UUID) 및 현재 세션 추적용 컬럼 추가
CREATE TABLE `cafe_table` (
                              `id`                 INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              `table_number`       INT          NOT NULL UNIQUE COMMENT '표시 테이블 번호',
                              `password`           VARCHAR(100) NOT NULL COMMENT '태블릿 최초 인증 비밀번호',
                              `status`             ENUM ('EMPTY','OCCUPIED','CLEANING') NOT NULL DEFAULT 'EMPTY',
                              `access_token`       VARCHAR(255) DEFAULT NULL UNIQUE COMMENT 'UUID 기반 자동 로그인 토큰',
                              `current_session_id` BIGINT       DEFAULT NULL COMMENT '현재 진행 중인 table_session ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='카페 물리 테이블 정보.';

-- 3. customer
CREATE TABLE `customer` (
                            `id`         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `phone`      VARCHAR(20) NOT NULL UNIQUE COMMENT '전화번호 (유일 식별자)',
                            `is_active`  BOOLEAN     NOT NULL DEFAULT TRUE,
                            `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='전화번호 등록 고객 정보.';

-- 4. category
-- 수정사항: 인원 추가 관리를 위한 GUEST 타입 확장
CREATE TABLE `category` (
                            `id`   INT                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `name` VARCHAR(50)                  NOT NULL COMMENT '카테고리명',
                            `type` ENUM ('DRINK','FOOD','GAME','GUEST') NOT NULL COMMENT 'GUEST: 인원추가 전용'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='메뉴·게임·인원 공통 대분류.';

-- 21. cafe_package (순서 조정: session 참조용)
CREATE TABLE `cafe_package` (
                                `id`                  INT                                 NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `name`                VARCHAR(50)                         NOT NULL,
                                `type`                ENUM ('HOURLY','FIXED_TIME','FREE') NOT NULL,
                                `duration_minutes`    INT                                          DEFAULT NULL,
                                `base_price`          INT                                 NOT NULL DEFAULT 0 COMMENT '1인당 기본 요금',
                                `extra_price_per_min` DECIMAL(7, 2)                                DEFAULT NULL,
                                `is_active`           BOOLEAN                             NOT NULL DEFAULT TRUE,
                                `updated_at`          TIMESTAMP                           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='패키지 요금 정책.';

-- 11. table_session [NEW]
-- 추가이유: 요구하신 '테이블 이용 이력 히스토리' 및 세션 로그인 유지를 위한 핵심 테이블
CREATE TABLE `table_session` (
                                 `id`                BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                 `table_id`          INT       NOT NULL COMMENT '이용 테이블 (FK)',
                                 `package_id`        INT       NOT NULL COMMENT '선택 패키지 (FK)',
                                 `initial_guest_cnt` INT       NOT NULL DEFAULT 1 COMMENT '최초 입장 인원',
                                 `check_in_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '입장 시간',
                                 `check_out_time`    TIMESTAMP NULL COMMENT '퇴장 시간',
                                 `is_active`         BOOLEAN   NOT NULL DEFAULT TRUE COMMENT '현재 세션 활성화 여부',
                                 `total_amount`      INT       NOT NULL DEFAULT 0 COMMENT '최종 정산 금액 (퇴실 시 합산)',
                                 CONSTRAINT `fk_session_table` FOREIGN KEY (`table_id`) REFERENCES `cafe_table` (`id`),
                                 CONSTRAINT `fk_session_package` FOREIGN KEY (`package_id`) REFERENCES `cafe_package` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='테이블 이용 세션 및 방문 히스토리.';

-- 5. menu
-- 수정사항: 추가 인원(GUEST) 상품이 이 테이블에 등록됨
CREATE TABLE `menu` (
                        `id`           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `category_id`  INT                   DEFAULT NULL,
                        `name`         VARCHAR(100) NOT NULL,
                        `price`        INT          NOT NULL COMMENT '판매 가격',
                        `description`  TEXT                  DEFAULT NULL,
                        `image_url`    VARCHAR(255)          DEFAULT NULL,
                        `is_available` BOOLEAN      NOT NULL DEFAULT TRUE,
                        `is_deleted`   BOOLEAN      NOT NULL DEFAULT FALSE,
                        `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT `fk_menu_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='판매 메뉴 및 인원추가 상품.';

-- 12. orders
-- 수정사항: session_id를 추가하여 '어느 방문 건'의 주문인지 명확히 식별
CREATE TABLE `orders` (
                          `id`             INT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          `session_id`     BIGINT    NOT NULL COMMENT '방문 세션 ID (FK)',
                          `table_id`       INT       NOT NULL COMMENT '주문 테이블 (FK)',
                          `customer_phone` VARCHAR(20)        DEFAULT NULL,
                          `status`         ENUM ('PENDING', 'PAID', 'CONFIRMED', 'COOKING', 'DELIVERING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PAID',
                          `total_amount`   INT       NOT NULL DEFAULT 0 COMMENT '주문 총액',
                          `ordered_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT `fk_orders_session` FOREIGN KEY (`session_id`) REFERENCES `table_session` (`id`),
                          CONSTRAINT `fk_orders_table` FOREIGN KEY (`table_id`) REFERENCES `cafe_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='주문 헤더.';

-- 13. order_item
CREATE TABLE `order_item` (
                              `id`        INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              `order_id`  INT          NOT NULL,
                              `menu_id`   INT                  DEFAULT NULL,
                              `menu_name` VARCHAR(100) NOT NULL,
                              `price`     INT          NOT NULL,
                              `quantity`  INT          NOT NULL,
                              CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='주문 상세 항목.';

-- 6. game / 7. game_item
CREATE TABLE `game` (
                        `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `category_id` INT                  DEFAULT NULL,
                        `name`        VARCHAR(100) NOT NULL,
                        `min_players` INT,
                        `max_players` INT,
                        `play_time`   INT,
                        `is_active`   BOOLEAN      NOT NULL DEFAULT TRUE,
                        CONSTRAINT `fk_game_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `game_item` (
                             `id`            INT                                       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             `game_id`       INT                                       NOT NULL,
                             `serial_number` VARCHAR(50)                               NOT NULL UNIQUE,
                             `status`        ENUM ('NORMAL','RENTED','DAMAGED','LOST') NOT NULL DEFAULT 'NORMAL',
                             CONSTRAINT `fk_gameitem_game` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. cart / 9. cart_item
CREATE TABLE `cart` (
                        `id`         INT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `table_id`   INT       NOT NULL UNIQUE,
                        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT `fk_cart_table` FOREIGN KEY (`table_id`) REFERENCES `cafe_table` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `cart_item` (
                             `id`       INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             `cart_id`  INT NOT NULL,
                             `menu_id`  INT NOT NULL,
                             `quantity` INT NOT NULL DEFAULT 1,
                             UNIQUE KEY `uq_cartitem_cart_menu` (`cart_id`, `menu_id`),
                             CONSTRAINT `fk_cartitem_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`) ON DELETE CASCADE,
                             CONSTRAINT `fk_cartitem_menu` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 22. rental_log
-- 수정사항: table_id 대신 session_id를 사용하여 히스토리 추적성 강화
CREATE TABLE `rental_log` (
                              `id`           BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              `session_id`   BIGINT    NOT NULL COMMENT '방문 세션 ID (FK)',
                              `game_item_id` INT       NOT NULL,
                              `rented_at`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              `returned_at`  TIMESTAMP NULL,
                              `status`       ENUM ('RENTING','RETURNED','DAMAGED','LOST') NOT NULL DEFAULT 'RENTING',
                              CONSTRAINT `fk_rental_session` FOREIGN KEY (`session_id`) REFERENCES `table_session` (`id`),
                              CONSTRAINT `fk_rental_item` FOREIGN KEY (`game_item_id`) REFERENCES `game_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='게임 대여 이력 로그.';

-- 14. payment / 15. toss_payment
CREATE TABLE `payment` (
                           `id`           INT                   NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `session_id`   BIGINT                NOT NULL UNIQUE COMMENT '세션당 최종 1회 결제',
                           `status`       ENUM ('READY','DONE') NOT NULL DEFAULT 'READY',
                           `final_amount` INT                   NOT NULL,
                           `paid_at`      TIMESTAMP             DEFAULT NULL,
                           CONSTRAINT `fk_payment_session` FOREIGN KEY (`session_id`) REFERENCES `table_session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `toss_payment` (
                                `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `payment_id`    INT          NOT NULL UNIQUE,
                                `payment_key`   VARCHAR(200) NOT NULL UNIQUE,
                                `order_id_toss` VARCHAR(64)  NOT NULL,
                                `method`        ENUM ('간편결제','계좌이체') DEFAULT NULL,
                                `raw_response`  JSON                 DEFAULT NULL,
                                `approved_at`   TIMESTAMP            DEFAULT NULL,
                                CONSTRAINT `fk_toss_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 16. point / 17. point_history
CREATE TABLE `point` (
                         `id`         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `phone`      VARCHAR(20) NOT NULL UNIQUE,
                         `balance`    INT         NOT NULL DEFAULT 0,
                         `updated_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `point_history` (
                                 `id`            BIGINT              NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                 `point_id`      INT                 NOT NULL,
                                 `order_id`      INT                          DEFAULT NULL,
                                 `type`          ENUM ('EARN','USE') NOT NULL,
                                 `amount`        INT                 NOT NULL,
                                 `balance_after` INT                 NOT NULL,
                                 `created_at`    TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT `fk_pointhistory_point` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`) ON DELETE CASCADE,
                                 CONSTRAINT `fk_pointhistory_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. macro_message / 18. table_message
CREATE TABLE `macro_message` (
                                 `id`           INT                                      NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                 `direction`    ENUM ('STAFF_TO_TABLE','TABLE_TO_STAFF') NOT NULL,
                                 `message_text` VARCHAR(255)                             NOT NULL,
                                 `is_active`    BOOLEAN                                  NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `table_message` (
                                 `id`         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                 `table_id`   INT          NOT NULL,
                                 `macro_id`   INT                   DEFAULT NULL,
                                 `content`    VARCHAR(255) NOT NULL,
                                 `is_read`    BOOLEAN      NOT NULL DEFAULT FALSE,
                                 `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT `fk_tablemsg_table` FOREIGN KEY (`table_id`) REFERENCES `cafe_table` (`id`),
                                 CONSTRAINT `fk_tablemsg_macro` FOREIGN KEY (`macro_id`) REFERENCES `macro_message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 19. item_sales_history / 20. daily_sales_summary
CREATE TABLE `item_sales_history` (
                                      `id`           INT                            NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                      `stat_date`    DATE                           NOT NULL,
                                      `product_id`   INT                            NOT NULL,
                                      `category`     ENUM ('DRINK', 'FOOD', 'GAME', 'GUEST') NOT NULL,
                                      `sales_qty`    INT                            NOT NULL DEFAULT 0,
                                      `sales_amount` BIGINT                         NOT NULL DEFAULT 0,
                                      UNIQUE KEY `uq_stat_date_product` (`stat_date`, `product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `daily_sales_summary` (
                                       `id`             BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       `stat_date`      DATE   NOT NULL UNIQUE,
                                       `total_revenue`  BIGINT NOT NULL DEFAULT 0,
                                       `order_count`    INT    NOT NULL DEFAULT 0,
                                       `visit_count`    INT    NOT NULL DEFAULT 0,
                                       `avg_usage_time` INT             DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
--  보드게임 카페 키오스크 — 더미 데이터 (board_cafe_kiosk_2603)
--  생성일: 2026-03-26
--  순서: FK 의존 관계에 따라 정렬
-- ============================================================

-- ============================================================
--  보드게임 카페 키오스크 — 더미 데이터 (board_cafe_kiosk_2603)
--  생성일: 2026-03-26
--  순서: FK 의존 관계에 따라 정렬
-- ============================================================

USE `board_cafe_kiosk_2603`;

-- ============================================================
-- 1. manager (관리자·직원)
-- ============================================================
INSERT INTO `manager` (`login_id`, `password`, `name`, `role`, `is_active`)
VALUES ('admin', '1111', '김민준', 'ADMIN', TRUE),
       ('staff01', '1111', '이서연', 'STAFF', TRUE),
       ('staff02', '1111', '박지호', 'STAFF', TRUE),
       ('staff03', '1111', '최유나', 'STAFF', FALSE);

-- ============================================================
-- 2. cafe_table (물리적 테이블 8개)
-- ============================================================
INSERT INTO `cafe_table` (`table_number`, `password`, `status`, `access_token`, `current_session_id`)
VALUES (1, 'table1234', 'OCCUPIED', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', NULL),
       (2, 'table1234', 'OCCUPIED', 'a1b2c3d4-e5f6-7890-abcd-ef1234567802', NULL),
       (3, 'table1234', 'EMPTY', 'a1b2c3d4-e5f6-7890-abcd-ef1234567803', NULL),
       (4, 'table1234', 'EMPTY', 'a1b2c3d4-e5f6-7890-abcd-ef1234567804', NULL),
       (5, 'table1234', 'OCCUPIED', 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', NULL),
       (6, 'table1234', 'CLEANING', 'a1b2c3d4-e5f6-7890-abcd-ef1234567806', NULL),
       (7, 'table1234', 'EMPTY', 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', NULL),
       (8, 'table1234', 'OCCUPIED', 'a1b2c3d4-e5f6-7890-abcd-ef1234567808', NULL),
        (9,  'table1234', 'EMPTY', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', NULL),
        (10, 'table1234', 'EMPTY', 'a1b2c3d4-e5f6-7890-abcd-ef1234567810', NULL),
        (11, 'table1234', 'EMPTY', 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', NULL),
        (12, 'table1234', 'EMPTY', 'a1b2c3d4-e5f6-7890-abcd-ef1234567812', NULL);

-- ============================================================
-- 3. customer (등록 고객)
-- ============================================================
INSERT INTO `customer` (`phone`, `is_active`)
VALUES ('010-1234-5678', TRUE),
       ('010-2345-6789', TRUE),
       ('010-3456-7890', TRUE),
       ('010-4567-8901', TRUE),
       ('010-5678-9012', TRUE),
       ('010-6789-0123', TRUE),
       ('010-7890-1234', FALSE);

-- ============================================================
-- 4. category (카테고리)
-- ============================================================
INSERT INTO `category` (`name`, `type`)
VALUES ('커피·에스프레소', 'DRINK'), -- 1
       ('논커피·에이드', 'DRINK'),  -- 2
       ('스낵·과자', 'FOOD'),     -- 3
       ('식사류', 'FOOD'),       -- 4
       ('전략 게임', 'GAME'),     -- 5
       ('파티 게임', 'GAME'),     -- 6
       ('협력 게임', 'GAME'),     -- 7
       ('추가 인원', 'GUEST');
-- 8

-- ============================================================
-- 21. cafe_package (요금 정책)
-- ============================================================
INSERT INTO `cafe_package` (`name`, `type`, `duration_minutes`, `base_price`, `extra_price_per_min`, `is_active`)
VALUES ('1시간 패키지', 'HOURLY', 60, 5000, NULL, TRUE),
       ('2시간 패키지', 'FIXED_TIME', 120, 8000, NULL, TRUE),
       ('3시간 패키지', 'FIXED_TIME', 180, 11000, NULL, TRUE),
       ('종일 자유이용권', 'FREE', NULL, 15000, NULL, TRUE),
       ('초과 시간 요금', 'HOURLY', 60, 2000, 35.00, FALSE);

-- ============================================================
-- 11. table_session (이용 세션)
-- ============================================================
INSERT INTO `table_session` (`table_id`, `package_id`, `initial_guest_cnt`, `check_in_time`, `check_out_time`,
                             `is_active`, `total_amount`)
VALUES (1, 2, 2, '2026-03-25 13:00:00', '2026-03-25 15:10:00', FALSE, 24500), -- 1
       (2, 3, 4, '2026-03-25 15:30:00', '2026-03-25 18:45:00', FALSE, 58000), -- 2
       (3, 1, 1, '2026-03-25 17:00:00', '2026-03-25 18:05:00', FALSE, 7500),  -- 3
       (5, 2, 3, '2026-03-25 19:00:00', '2026-03-25 21:15:00', FALSE, 35000), -- 4
       (8, 4, 5, '2026-03-25 11:00:00', '2026-03-25 23:00:00', FALSE, 95000), -- 5
       (1, 2, 2, '2026-03-26 13:30:00', NULL, TRUE, 0),                       -- 6
       (2, 3, 3, '2026-03-26 14:00:00', NULL, TRUE, 0),                       -- 7
       (5, 1, 2, '2026-03-26 15:00:00', NULL, TRUE, 0),                       -- 8
       (8, 2, 4, '2026-03-26 12:00:00', NULL, TRUE, 0);
-- 9

-- 세션 연결 업데이트
UPDATE `cafe_table`
SET `current_session_id` = 6
WHERE `table_number` = 1;
UPDATE `cafe_table`
SET `current_session_id` = 7
WHERE `table_number` = 2;
UPDATE `cafe_table`
SET `current_session_id` = 8
WHERE `table_number` = 5;
UPDATE `cafe_table`
SET `current_session_id` = 9
WHERE `table_number` = 8;

-- ============================================================
-- 5. menu (음식·음료 + 추가인원 상품)
-- ============================================================
INSERT INTO `menu` (`category_id`, `name`, `price`, `description`, `is_available`)
VALUES (1, '아메리카노', 3000, '깔끔하고 진한 에스프레소 베이스', TRUE),   -- 1
       (1, '카페라떼', 3500, '우유와 에스프레소의 조화', TRUE),        -- 2
       (1, '카푸치노', 3500, '풍성한 우유 거품과 에스프레소', TRUE),     -- 3
       (1, '바닐라라떼', 4000, '달콤한 바닐라 시럽 추가', TRUE),       -- 4
       (2, '레몬에이드', 4000, '상큼한 국산 레몬 착즙', TRUE),        -- 5
       (2, '자몽에이드', 4000, '달콤 쌉싸름한 자몽 에이드', TRUE),      -- 6
       (2, '녹차라떼', 3500, '국내산 말차 분말 사용', TRUE),         -- 7
       (2, '유자차', 3500, '따뜻하게도 아이스로도', TRUE),           -- 8
       (3, '팝콘 (오리지널)', 2000, '고소한 버터 팝콘', TRUE),       -- 9
       (3, '팝콘 (카라멜)', 2500, '달콤한 카라멜 코팅', TRUE),       -- 10
       (3, '나초 + 살사소스', 3000, '바삭한 나초와 살사소스 콤보', TRUE), -- 11
       (3, '믹스 너트', 3500, '7가지 프리미엄 너트 혼합', TRUE),      -- 12
       (4, '토스트 세트', 5000, '계란 토스트 + 음료 세트', TRUE),     -- 13
       (4, '컵라면', 1500, '신라면·짜파게티 선택 가능', TRUE),        -- 14
       (4, '핫도그', 3000, '국산 돼지고기 소시지 사용', FALSE),       -- 15
       (8, '인원 추가 (1명)', 5000, '기본 패키지 인당 추가 요금', TRUE);
-- 16

-- [게임/대여/장바구니/메시지 데이터는 논리적으로 완벽하여 생략 후 order_item 부분으로 넘어감]
-- (실제 실행 시에는 보내주신 원본의 6, 7, 8, 9, 10 섹션을 그대로 넣으시면 됩니다.)

-- (중간 생략: game, game_item, cart, cart_item, macro_message 삽입)

-- ============================================================
-- 12. orders (주문 헤더)
-- ============================================================
INSERT INTO `orders` (`session_id`, `table_id`, `customer_phone`, `status`, `total_amount`, `ordered_at`)
VALUES (1, 1, '010-1234-5678', 'COMPLETED', 13000, '2026-03-25 13:10:00'), -- id=1
       (1, 1, '010-1234-5678', 'COMPLETED', 7500, '2026-03-25 14:00:00'),  -- id=2
       (2, 2, '010-2345-6789', 'COMPLETED', 34000, '2026-03-25 15:45:00'), -- id=3
       (2, 2, NULL, 'COMPLETED', 10000, '2026-03-25 17:00:00'),            -- id=4
       (3, 3, '010-3456-7890', 'COMPLETED', 5000, '2026-03-25 17:10:00'),  -- id=5
       (4, 5, '010-4567-8901', 'COMPLETED', 21500, '2026-03-25 19:15:00'), -- id=6
       (5, 8, NULL, 'COMPLETED', 42000, '2026-03-25 12:00:00'),            -- id=7
       (6, 1, '010-1234-5678', 'COMPLETED', 10000, '2026-03-26 13:40:00'), -- id=8
       (7, 2, '010-5678-9012', 'COMPLETED', 15500, '2026-03-26 14:20:00'), -- id=9
       (8, 5, NULL, 'COOKING', 7000, '2026-03-26 15:10:00'),               -- id=10
       (9, 8, '010-6789-0123', 'PENDING', 21000, '2026-03-26 12:30:00');
-- id=11

-- ============================================================
-- 13. order_item (주문 상세 항목)
-- ============================================================
INSERT INTO `order_item` (`order_id`, `menu_id`, `menu_name`, `price`, `quantity`)
VALUES (1, 1, '아메리카노', 3000, 2),
       (1, 9, '팝콘 (오리지널)', 2000, 1),
       (1, 13, '토스트 세트', 5000, 1),
       (2, 5, '레몬에이드', 4000, 1),
       (2, 12, '믹스 너트', 3500, 1),
       (3, 2, '카페라떼', 3500, 4),
       (3, 10, '팝콘 (카라멜)', 2500, 2),
       (3, 13, '토스트 세트', 5000, 3),
       (4, 7, '녹차라떼', 3500, 2),
       (4, 14, '컵라면', 1500, 2),
       (5, 1, '아메리카노', 3000, 1),
       (5, 9, '팝콘 (오리지널)', 2000, 1),
       (6, 3, '카푸치노', 3500, 3),
       (6, 11, '나초 + 살사소스', 3000, 2),
       (6, 16, '인원 추가 (1명)', 5000, 1),
       (7, 4, '바닐라라떼', 4000, 4),
       (7, 9, '팝콘 (오리지널)', 2000, 5),
       (7, 13, '토스트 세트', 5000, 4),
       (8, 1, '아메리카노', 3000, 2),
       (8, 6, '자몽에이드', 4000, 1),
       (9, 2, '카페라떼', 3500, 3),
       (9, 13, '토스트 세트', 5000, 1),
       (10, 5, '레몬에이드', 4000, 1), -- id=10번 주문 항목 추가
       (10, 14, '컵라면', 1500, 2),
       (11, 4, '바닐라라떼', 4000, 4), -- id=11번 주문 항목 수정
       (11, 10, '팝콘 (카라멜)', 2500, 2);

-- ============================================================
-- game 관련 더미 데이터 추가
-- ============================================================

-- ============================================================
-- 10. game (보드게임 종목 — MenuService.getGameItems() 이름과 일치)
-- ============================================================
--   category: 5=전략 게임, 6=파티 게임, 7=협력 게임
INSERT INTO `game` (`category_id`, `name`, `min_players`, `max_players`, `play_time`, `is_active`)
VALUES (6, '맞춤법 게임', 2, 6, 20,  TRUE),  -- id=1  stock=NORMAL 3개
       (6, '숫자 맞추기', 2, 4, 15,  TRUE),  -- id=2  stock=NORMAL 2개
       (6, '동물 맞추기', 2, 6, 20,  TRUE),  -- id=3  stock=NORMAL 0개 (전부 대여중/파손)
       (7, '색상 맞추기', 2, 5, 25,  TRUE),  -- id=4  stock=NORMAL 1개
       (5, '스피드 게임',  2, 8, 10,  TRUE),  -- id=5  stock=NORMAL 0개 (전부 대여중)
       (6, '퀴즈 게임',   2, 10, 30, TRUE);  -- id=6  stock=NORMAL 4개

-- ============================================================
-- 11. game_item (실물 박스 재고 — status별 합산이 game별 stock)
-- ============================================================
--  NORMAL = 대여 가능 / RENTED = 현재 대여 중 / DAMAGED = 파손 / LOST = 분실
INSERT INTO `game_item` (`game_id`, `serial_number`, `status`)
VALUES
-- 맞춤법 게임 (game_id=1): NORMAL 3개
(1, 'SPL-001', 'NORMAL'),
(1, 'SPL-002', 'NORMAL'),
(1, 'SPL-003', 'NORMAL'),
(1, 'SPL-004', 'RENTED'),   -- 현재 대여 중
(1, 'SPL-005', 'DAMAGED'),  -- 파손

-- 숫자 맞추기 (game_id=2): NORMAL 2개
(2, 'NUM-001', 'NORMAL'),
(2, 'NUM-002', 'NORMAL'),
(2, 'NUM-003', 'RENTED'),   -- 현재 대여 중

-- 동물 맞추기 (game_id=3): NORMAL 0개 (전부 대여중 or 파손)
(3, 'ANM-001', 'RENTED'),
(3, 'ANM-002', 'RENTED'),
(3, 'ANM-003', 'DAMAGED'),

-- 색상 맞추기 (game_id=4): NORMAL 1개
(4, 'CLR-001', 'NORMAL'),
(4, 'CLR-002', 'RENTED'),   -- 현재 대여 중
(4, 'CLR-003', 'LOST'),     -- 분실

-- 스피드 게임 (game_id=5): NORMAL 0개 (전부 대여중)
(5, 'SPD-001', 'RENTED'),
(5, 'SPD-002', 'RENTED'),

-- 퀴즈 게임 (game_id=6): NORMAL 4개
(6, 'QUZ-001', 'NORMAL'),
(6, 'QUZ-002', 'NORMAL'),
(6, 'QUZ-003', 'NORMAL'),
(6, 'QUZ-004', 'NORMAL'),
(6, 'QUZ-005', 'RENTED');   -- 현재 대여 중

-- ============================================================
-- 12. rental_log (게임 대여 이력 — 현재 대여 중인 RENTED 항목 반영)
-- ============================================================
--  현재 대여 중(is_active=TRUE) 세션: 6(table1), 7(table2), 8(table5), 9(table8)
INSERT INTO `rental_log` (`session_id`, `game_item_id`, `rented_at`, `returned_at`, `status`)
VALUES
-- 과거 세션 반납 완료 기록
(1, 4,  '2026-03-25 13:05:00', '2026-03-25 14:50:00', 'RETURNED'),  -- 맞춤법 SPL-004
(2, 9,  '2026-03-25 15:35:00', '2026-03-25 18:30:00', 'RETURNED'),  -- 동물 ANM-001
(3, 6,  '2026-03-25 17:05:00', '2026-03-25 18:00:00', 'RETURNED'),  -- 숫자 NUM-001
(4, 12, '2026-03-25 19:05:00', '2026-03-25 21:10:00', 'RETURNED'),  -- 색상 CLR-002
(5, 15, '2026-03-25 11:05:00', '2026-03-25 22:50:00', 'RETURNED'),  -- 스피드 SPD-001

-- 현재 대여 중 (session_id 6~9, returned_at=NULL)
(6,  4, '2026-03-26 13:35:00', NULL, 'RENTING'),  -- table1: 맞춤법 SPL-004
(7,  9, '2026-03-26 14:05:00', NULL, 'RENTING'),  -- table2: 동물 ANM-001
(7, 10, '2026-03-26 14:05:00', NULL, 'RENTING'),  -- table2: 동물 ANM-002
(8, 12, '2026-03-26 15:05:00', NULL, 'RENTING'),  -- table5: 색상 CLR-002
(8, 15, '2026-03-26 15:05:00', NULL, 'RENTING'),  -- table5: 스피드 SPD-001
(8, 16, '2026-03-26 15:05:00', NULL, 'RENTING'),  -- table5: 스피드 SPD-002
(9,  7, '2026-03-26 12:05:00', NULL, 'RENTING'),  -- table8: 숫자 NUM-003
(9, 20, '2026-03-26 12:05:00', NULL, 'RENTING');  -- table8: 퀴즈 QUZ-005

-- ============================================================
-- 10. macro_message
-- ============================================================
INSERT INTO `macro_message` (`direction`, `message_text`, `is_active`)
VALUES
/* STAFF_TO_TABLE: 직원이 테이블(키오스크)로 보내는 알림 */
    ('STAFF_TO_TABLE', '주문하신 음료와 스낵이 준비되었습니다. 카운터에서 수령해 주세요.', TRUE),
    ('STAFF_TO_TABLE', '이용 시간이 10분 남았습니다. 연장을 원하시면 카운터에 문의해 주세요.', TRUE),
    ('STAFF_TO_TABLE', '주문하신 메뉴가 품절되어 취소 처리되었습니다. 죄송합니다.', TRUE),
    ('STAFF_TO_TABLE', '현재 보드게임 반납 구역이 혼잡하오니 테이블에 그대로 두시면 치워드리겠습니다.', TRUE),
    ('STAFF_TO_TABLE', '진행 중인 이벤트에 당첨되셨습니다! 카운터에서 선물을 확인하세요.', TRUE),
    ('STAFF_TO_TABLE', '외부 음식 반입은 금지되어 있습니다. 양해 부탁드립니다.', TRUE),

/* TABLE_TO_STAFF: 고객이 직원(관리자 페이지)에게 보내는 요청 */
    ('TABLE_TO_STAFF', '게임 설명이 필요합니다. 직원을 호출해 주세요.', TRUE),
    ('TABLE_TO_STAFF', '테이블이 지저분합니다. 청소 부탁드려요.', TRUE),
    ('TABLE_TO_STAFF', '물티슈나 티슈가 부족합니다. 가져다주세요.', TRUE),
    ('TABLE_TO_STAFF', '에어컨/히터 온도 조절 부탁드립니다. (추워요/더워요)', TRUE),
    ('TABLE_TO_STAFF', '음료를 쏟았습니다. 도움이 필요합니다.', TRUE),
    ('TABLE_TO_STAFF', '결제 방식 변경이나 오류 문의로 호출합니다.', TRUE);