package org.example.board_cafe_kiosk_2603.service.admin.product;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 작성자 : 서주연
 * 기능 : GameServiceImpl 테스트
 * 날짜 : 2026-03-30
 */

@Log4j2
@SpringBootTest
@Transactional
class GameServiceImplTest {

    @Autowired
    private GameService gameService;

    private GameResponseDTO createGame(String name) {
        GameRequestDTO request = GameRequestDTO.builder()
                .categoryId(5)
                .name(name)
                .minPlayers(2)
                .maxPlayers(4)
                .playTime(30)
                .isActive(true)
                .build();
        gameService.register(request);
        return gameService.getAll().stream()
                .filter(game -> name.equals(game.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("생성한 게임을 찾지 못했습니다."));
    }

    @Test
    void getAllTest() {
        List<GameResponseDTO> list = gameService.getAll();
        list.forEach(game -> log.info(game));
    }

    @Test
    void getByCategoryIdTest() {
        List<GameResponseDTO> list = gameService.getByCategoryId(1);
        list.forEach(game -> log.info(game));
    }

    @Test
    void getByIsActiveTest() {
        List<GameResponseDTO> list = gameService.getByIsActive(true);
        list.forEach(game -> log.info(game));
    }

    @Test
    void getByIdTest() {
        GameResponseDTO gameResponseDTO = gameService.getById(1);
        log.info(gameResponseDTO);
    }

    @Test
    void registerTest() {
        GameRequestDTO gameRequestDTO = GameRequestDTO.builder()
                .categoryId(5)
                .name("테스트게임_" + System.currentTimeMillis())
                .minPlayers(2)
                .maxPlayers(4)
                .playTime(30)
                .isActive(true)
                .build();
        gameService.register(gameRequestDTO);
        log.info("register 완료");
    }

    @Test
    void modifyTest() {
        GameResponseDTO created = createGame("수정전게임_" + System.currentTimeMillis());
        GameRequestDTO gameRequestDTO = GameRequestDTO.builder()
                .categoryId(created.getCategoryId())
                .name("수정된게임")
                .minPlayers(2)
                .maxPlayers(6)
                .playTime(60)
                .isActive(true)
                .build();
        gameService.modify(created.getId(), gameRequestDTO);
        log.info("modify 완료");
    }

    @Test
    void removeTest() {
        GameResponseDTO created = createGame("삭제용게임_" + System.currentTimeMillis());
        gameService.remove(created.getId());
        log.info("remove 완료");
    }

    @Test
    void toggleActiveTest() {
        GameResponseDTO created = createGame("활성전환게임_" + System.currentTimeMillis());
        gameService.toggleActive(created.getId());
        log.info("toggleActive 완료");
    }
}
