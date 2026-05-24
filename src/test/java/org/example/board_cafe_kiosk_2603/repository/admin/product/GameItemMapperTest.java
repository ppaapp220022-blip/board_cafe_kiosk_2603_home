package org.example.board_cafe_kiosk_2603.repository.admin.product;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItem;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItemStatus;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemResponseDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.product.GameItemMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * 작성자 : 서주연
 * 기능 : GameItemMapper 테스트
 * 날짜 : 2026-03-30
 */

@Log4j2
@SpringBootTest
@Transactional
class GameItemMapperTest {

    @Autowired
    private GameItemMapper gameItemMapper;

    private GameItem createGameItem(String serialNumber) {
        GameItem gameItem = GameItem.builder()
                .gameId(1)
                .serialNumber(serialNumber)
                .status(GameItemStatus.NORMAL)
                .build();
        gameItemMapper.insert(gameItem);
        return gameItem;
    }

    /*
     * 작성자 : 서주연
     * 기능 : findAllTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void findAllTest() {
        List<GameItemResponseDTO> list = gameItemMapper.findAll();
        assertTrue(list.stream().allMatch(item -> item.getGameName() != null && !item.getGameName().isBlank()));
        list.forEach(item -> log.info(item));
    }

    /*
     * 작성자 : 서주연
     * 기능 : findByGameIdTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void findByGameIdTest() {
        List<GameItemResponseDTO> list = gameItemMapper.findByGameId(1);
        list.forEach(item -> log.info(item));
    }

    /*
     * 작성자 : 서주연
     * 기능 : findByStatusTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void findByStatusTest() {
        List<GameItemResponseDTO> list = gameItemMapper.findByStatus(GameItemStatus.NORMAL);
        assertTrue(list.stream().allMatch(item -> item.getStatus() == GameItemStatus.NORMAL));
        list.forEach(item -> log.info(item));
    }

    /*
     * 작성자 : 서주연
     * 기능 : findByIdTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void findByIdTest() {
        Optional<GameItemResponseDTO> item = gameItemMapper.findById(1);
        item.ifPresent(gameItem -> assertNotNull(gameItem.getGameName()));
        log.info(item);
    }

    /*
     * 작성자 : 서주연
     * 기능 : insertTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void insertTest() {
        GameItem gameItem = GameItem.builder()
                .gameId(1)
                .serialNumber("SN-TEST-" + System.currentTimeMillis())
                .status(GameItemStatus.NORMAL)
                .build();
        int result = gameItemMapper.insert(gameItem);
        log.info("insert 결과: " + result);
    }

    /*
     * 작성자 : 서주연
     * 기능 : updateTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void updateTest() {
        GameItem created = createGameItem("SN-BEFORE-" + System.currentTimeMillis());
        GameItem gameItem = GameItem.builder()
                .id(created.getId())
                .gameId(created.getGameId())
                .serialNumber("SN-TEST-002")
                .status(GameItemStatus.RENTED)
                .build();
        int result = gameItemMapper.update(gameItem);
        log.info("update 결과: " + result);
    }

    /*
     * 작성자 : 서주연
     * 기능 : deleteTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void deleteTest() {
        GameItem created = createGameItem("SN-DELETE-" + System.currentTimeMillis());
        int result = gameItemMapper.delete(created.getId());
        log.info("delete 결과: " + result);
    }

    /*
     * 작성자 : 서주연
     * 기능 : updateStatusTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void updateStatusTest() {
        GameItem created = createGameItem("SN-STATUS-" + System.currentTimeMillis());
        int result = gameItemMapper.updateStatus(created.getId(), GameItemStatus.DAMAGED);
        log.info("updateStatus 결과: " + result);
    }
}
