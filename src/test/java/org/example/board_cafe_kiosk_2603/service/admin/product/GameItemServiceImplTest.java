package org.example.board_cafe_kiosk_2603.service.admin.product;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItemStatus;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 작성자 : 서주연
 * 기능 : GameItemServiceImpl 테스트
 * 날짜 : 2026-03-30
 */

@Log4j2
@SpringBootTest
@Transactional
class GameItemServiceImplTest {

    @Autowired
    private GameItemService gameItemService;

    private GameItemResponseDTO createGameItem(String serialNumber) {
        GameItemRequestDTO request = GameItemRequestDTO.builder()
                .gameId(1)
                .serialNumber(serialNumber)
                .status(GameItemStatus.NORMAL)
                .build();
        gameItemService.register(request);
        return gameItemService.getAll().stream()
                .filter(item -> serialNumber.equals(item.getSerialNumber()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("생성한 테스트 아이템을 찾지 못했습니다."));
    }

    /*
     * 작성자 : 서주연
     * 기능 : getAllTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void getAllTest() {
        List<GameItemResponseDTO> list = gameItemService.getAll();
        list.forEach(item -> log.info(item));
    }

    /*
     * 작성자 : 서주연
     * 기능 : getByGameIdTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void getByGameIdTest() {
        List<GameItemResponseDTO> list = gameItemService.getByGameId(1);
        list.forEach(item -> log.info(item));
    }

    /*
     * 작성자 : 서주연
     * 기능 : getByStatusTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void getByStatusTest() {
        List<GameItemResponseDTO> list = gameItemService.getByStatus(GameItemStatus.NORMAL);
        list.forEach(item -> log.info(item));
    }

    /*
     * 작성자 : 서주연
     * 기능 : getByIdTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void getByIdTest() {
        GameItemResponseDTO gameItemResponseDTO = gameItemService.getById(1);
        log.info(gameItemResponseDTO);
    }

    /*
     * 작성자 : 서주연
     * 기능 : registerTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void registerTest() {
        GameItemRequestDTO gameItemRequestDTO = GameItemRequestDTO.builder()
                .gameId(1)
                .serialNumber("SN-TEST-" + System.currentTimeMillis())
                .status(GameItemStatus.NORMAL)
                .build();
        gameItemService.register(gameItemRequestDTO);
        log.info("register 완료");
    }

    /*
     * 작성자 : 서주연
     * 기능 : modifyTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void modifyTest() {
        GameItemResponseDTO created = createGameItem("SN-MODIFY-" + System.currentTimeMillis());
        GameItemRequestDTO gameItemRequestDTO = GameItemRequestDTO.builder()
                .gameId(1)
                .serialNumber("SN-UPDATED-" + System.currentTimeMillis())
                .status(GameItemStatus.RENTED)
                .build();
        gameItemService.modify(created.getId(), gameItemRequestDTO);
        log.info("modify 완료");
    }

    /*
     * 작성자 : 서주연
     * 기능 : removeTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void removeTest() {
        String serial = "SN-REMOVE-POLICY-" + System.currentTimeMillis();

        GameItemRequestDTO gameItemRequestDTO = GameItemRequestDTO.builder()
                .gameId(1)
                .serialNumber(serial)
                .status(GameItemStatus.NORMAL)
                .build();
        gameItemService.register(gameItemRequestDTO);

        GameItemResponseDTO created = gameItemService.getAll().stream()
                .filter(item -> serial.equals(item.getSerialNumber()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("방금 등록한 테스트 아이템을 찾지 못했습니다."));

        assertThrows(IllegalStateException.class, () -> gameItemService.remove(created.getId()));

        gameItemService.changeStatus(created.getId(), GameItemStatus.DAMAGED);
        assertDoesNotThrow(() -> gameItemService.remove(created.getId()));
        log.info("remove 정책 테스트 완료 - itemId: {}", created.getId());
    }

    /*
     * 작성자 : 서주연
     * 기능 : changeStatusTest 메서드
     * 날짜 : 2026-03-30
     */

    @Test
    void changeStatusTest() {
        GameItemResponseDTO created = createGameItem("SN-STATUS-" + System.currentTimeMillis());
        gameItemService.changeStatus(created.getId(), GameItemStatus.DAMAGED);
        log.info("changeStatus 완료");
    }
}
