DELIMITER $$

DROP PROCEDURE IF EXISTS generate_march_stats_data$$

CREATE PROCEDURE generate_march_stats_data()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE rand_date DATETIME;
    DECLARE rand_table_id INT;
    DECLARE rand_package_id INT;
    DECLARE rand_guest_cnt INT;
    DECLARE rand_duration INT;
    DECLARE last_session_id BIGINT;
    DECLARE last_order_id INT;
    DECLARE order_count_per_session INT;

    WHILE i <= 1000 DO
            SET rand_date = FROM_UNIXTIME(
                    UNIX_TIMESTAMP('2026-03-01 11:00:00') + FLOOR(RAND() * (UNIX_TIMESTAMP('2026-03-31 23:00:00') - UNIX_TIMESTAMP('2026-03-01 11:00:00')))
                            );

            SET rand_table_id = FLOOR(1 + (RAND() * 12));
            SET rand_package_id = FLOOR(1 + (RAND() * 4));
            SET rand_guest_cnt = FLOOR(1 + (RAND() * 5));
            SET rand_duration = FLOOR(60 + (RAND() * 180));

            INSERT INTO `table_session` (table_id, package_id, initial_guest_cnt, check_in_time, check_out_time, is_active, total_amount)
            VALUES (rand_table_id, rand_package_id, rand_guest_cnt, rand_date, DATE_ADD(rand_date, INTERVAL rand_duration MINUTE), FALSE, 0);

            SET last_session_id = LAST_INSERT_ID();

            SET order_count_per_session = FLOOR(1 + (RAND() * 2));
            WHILE order_count_per_session > 0 DO
                    INSERT INTO `orders` (session_id, table_id, status, total_amount, ordered_at)
                    VALUES (last_session_id, rand_table_id, 'COMPLETED', 0, DATE_ADD(rand_date, INTERVAL (order_count_per_session * 15) MINUTE));

                    SET last_order_id = LAST_INSERT_ID();

                    -- 메뉴 1~16번 중 랜덤 삽입
                    INSERT INTO `order_item` (order_id, menu_id, menu_name, price, quantity)
                    SELECT last_order_id, id, name, price, FLOOR(1 + (RAND() * 2))
                    FROM menu
                    WHERE id = FLOOR(1 + (RAND() * 16))
                    LIMIT 1;

                    -- [수정 포인트 1] SUM 결과가 NULL일 경우 0으로 치환
                    UPDATE `orders`
                    SET total_amount = (SELECT IFNULL(SUM(price * quantity), 0) FROM `order_item` WHERE order_id = last_order_id)
                    WHERE id = last_order_id;

                    SET order_count_per_session = order_count_per_session - 1;
                END WHILE;

            -- [수정 포인트 2] 세션 합계도 NULL 방지 처리
            UPDATE `table_session`
            SET total_amount = (SELECT IFNULL(SUM(total_amount), 0) FROM `orders` WHERE session_id = last_session_id)
            WHERE id = last_session_id;

            INSERT INTO `payment` (session_id, status, final_amount, paid_at)
            SELECT id, 'DONE', total_amount, check_out_time FROM `table_session` WHERE id = last_session_id;

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

-- 다시 실행
CALL generate_march_stats_data();

# -------------------------------------
# 실시간 통계 확인용 오늘치(26.04.03) 데이터
# -------------------------------------

DELIMITER $$

DROP PROCEDURE IF EXISTS generate_today_stats_data$$

CREATE PROCEDURE generate_today_stats_data()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE rand_date DATETIME;
    DECLARE rand_table_id INT;
    DECLARE rand_package_id INT;
    DECLARE rand_guest_cnt INT;
    DECLARE rand_duration INT;
    DECLARE last_session_id BIGINT;
    DECLARE last_order_id INT;
    DECLARE order_count_per_session INT;
    DECLARE is_extra_guest INT; -- 인원 추가 테스트를 위한 난수 변수

    WHILE i <= 100 DO
            -- [수정] 오늘 날짜(CURDATE()) 기준, 오전 10시 ~ 오후 10시 사이 랜덤 시간 생성
            SET rand_date = DATE_ADD(CURDATE(), INTERVAL FLOOR(10*60 + RAND() * 12*60) MINUTE);

            SET rand_table_id = FLOOR(1 + (RAND() * 12));
            SET rand_package_id = FLOOR(1 + (RAND() * 4));
            SET rand_guest_cnt = FLOOR(1 + (RAND() * 4)); -- 초기 인원은 1~4명
            SET rand_duration = FLOOR(60 + (RAND() * 120)); -- 1시간 ~ 3시간 이용

            -- 1. table_session 생성
            INSERT INTO `table_session` (table_id, package_id, initial_guest_cnt, check_in_time, check_out_time, is_active, total_amount)
            VALUES (rand_table_id, rand_package_id, rand_guest_cnt, rand_date, DATE_ADD(rand_date, INTERVAL rand_duration MINUTE), FALSE, 0);

            SET last_session_id = LAST_INSERT_ID();

            -- 2. 세션별 주문 생성 (1~3건)
            SET order_count_per_session = FLOOR(1 + (RAND() * 3));
            WHILE order_count_per_session > 0 DO
                    INSERT INTO `orders` (session_id, table_id, status, total_amount, ordered_at)
                    VALUES (last_session_id, rand_table_id, 'COMPLETED', 0, DATE_ADD(rand_date, INTERVAL (order_count_per_session * 15) MINUTE));

                    SET last_order_id = LAST_INSERT_ID();

                    -- [핵심 검증 포인트] 1~10 중 난수를 뽑아 3 이하(30% 확률)면 '인원 추가' 주문 발생
                    SET is_extra_guest = FLOOR(1 + (RAND() * 10));

                    IF is_extra_guest <= 3 THEN
                        -- 통계 로직 테스트용 '인원 추가 (1명)' 삽입 (1~2명 추가)
                        INSERT INTO `order_item` (order_id, menu_id, menu_name, price, quantity)
                        SELECT last_order_id, id, name, price, FLOOR(1 + (RAND() * 2))
                        FROM menu
                        WHERE name = '인원 추가 (1명)'
                        LIMIT 1;
                    ELSE
                        -- 일반 메뉴 랜덤 삽입 (인원 추가 메뉴 제외)
                        INSERT INTO `order_item` (order_id, menu_id, menu_name, price, quantity)
                        SELECT last_order_id, id, name, price, FLOOR(1 + (RAND() * 2))
                        FROM menu
                        WHERE name != '인원 추가 (1명)'
                        ORDER BY RAND() -- 남아있는 메뉴 중 무작위 1개
                        LIMIT 1;
                    END IF;

                    -- 주문 총액 업데이트 (NULL 방지)
                    UPDATE `orders`
                    SET total_amount = (SELECT IFNULL(SUM(price * quantity), 0) FROM `order_item` WHERE order_id = last_order_id)
                    WHERE id = last_order_id;

                    SET order_count_per_session = order_count_per_session - 1;
                END WHILE;

            -- 3. 세션 총액 업데이트 (NULL 방지)
            UPDATE `table_session`
            SET total_amount = (SELECT IFNULL(SUM(total_amount), 0) FROM `orders` WHERE session_id = last_session_id)
            WHERE id = last_session_id;

            -- 4. 결제 정보 생성
            INSERT INTO `payment` (session_id, status, final_amount, paid_at)
            SELECT id, 'DONE', total_amount, check_out_time FROM `table_session` WHERE id = last_session_id;

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

-- 오늘 자 100건 생성 실행!
CALL generate_today_stats_data();