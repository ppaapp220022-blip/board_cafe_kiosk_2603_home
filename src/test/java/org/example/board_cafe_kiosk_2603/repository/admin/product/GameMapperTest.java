package org.example.board_cafe_kiosk_2603.repository.admin.product;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.product.Game;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameResponseDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.product.GameMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * 작성자 : 서주연
 * 기능 : GameMapper 테스트
 * 날짜 : 2026-03-30
 */

@Log4j2
@SpringBootTest
@Transactional
class GameMapperTest {

    @Autowired
    private GameMapper gameMapper;

    private Game createGame(String name) {
        Game game = Game.builder()
                .categoryId(5)
                .name(name)
                .minPlayers(2)
                .maxPlayers(4)
                .playTime(30)
                .isActive(true)
                .build();
        gameMapper.insert(game);
        return game;
    }

    @Test
    void findAllTest() {
        List<GameResponseDTO> list = gameMapper.findAll();
        list.forEach(game -> log.info(game));
    }

    @Test
    void findByCategoryIdTest() {
        List<GameResponseDTO> list = gameMapper.findByCategoryId(1);
        list.forEach(game -> log.info(game));
    }

    @Test
    void findByIsActiveTest() {
        List<GameResponseDTO> list = gameMapper.findByIsActive(true);
        list.forEach(game -> log.info(game));
    }

    @Test
    void findByIdTest() {
        Optional<GameResponseDTO> game = gameMapper.findById(1);
        log.info(game);
    }

    @Test
    void insertTest() {
        Game game = Game.builder()
                .categoryId(5)
                .name("테스트게임_" + System.currentTimeMillis())
                .minPlayers(2)
                .maxPlayers(4)
                .playTime(30)
                .isActive(true)
                .build();
        int result = gameMapper.insert(game);
        log.info("insert 결과: " + result);
    }

    @Test
    void updateTest() {
        Game created = createGame("수정전게임_" + System.currentTimeMillis());
        Game game = Game.builder()
                .id(created.getId())
                .categoryId(created.getCategoryId())
                .name("수정된게임")
                .minPlayers(2)
                .maxPlayers(6)
                .playTime(60)
                .isActive(true)
                .build();
        int result = gameMapper.update(game);
        log.info("update 결과: " + result);
    }

    @Test
    void deleteTest() {
        Game created = createGame("삭제용게임_" + System.currentTimeMillis());
        int result = gameMapper.delete(created.getId());
        log.info("delete 결과: " + result);
    }

    @Test
    void toggleActiveTest() {
        Game created = createGame("활성전환게임_" + System.currentTimeMillis());
        int result = gameMapper.toggleActive(created.getId());
        log.info("toggleActive 결과: " + result);
    }
}
