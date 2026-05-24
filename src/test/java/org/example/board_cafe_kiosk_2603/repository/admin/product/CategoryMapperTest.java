package org.example.board_cafe_kiosk_2603.repository.admin.product;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.product.CategoryType;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryResponseDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.product.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


/*
 * 작성자 : 서주연
 * 기능 : CategoryMapper 테스트
 * 날짜 : 2026-03-30
 */

@Log4j2
@SpringBootTest
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;
    @Test
    void findAllTest() {
        List<CategoryResponseDTO> list = categoryMapper.findAll();
        log.info("=== 전체 카테고리 목록 ===");
        list.forEach(category -> log.info("전체 카테고리 목록: {}", category));
    }
    @Test
    void findByTypeTest() {
        List<CategoryResponseDTO> list = categoryMapper.findByType(CategoryType.GAME);
        log.info("=== type 기준 카테고리 조회 ===");
        list.forEach(category -> log.info("type 기준 카테고리 조회: {}", category));
    }
    @Test
    void findByIdTest() {
        // DB에 존재하는 id
        Optional<CategoryResponseDTO> category = categoryMapper.findById(1);
        log.info("카테고리 단건 조회: {}", category);
        // Optional[Category(id=1, name=커피·에스프레소, type=DRINK)]
    }

    /* 카테고리 - 등록 */
    // 새로운 카테고리 생성
//    @Test
//    void insertTest() {
//        Category category = Category.builder()
//                .name("테스트카테고리")
//                .type(CategoryType.GAME)
//                .build();
//        int result = categoryMapper.insert(category);
//        log.info("insert 결과: " + result);
//    }

    /* 카테고리 - 수정 */
    // 카테고리 이름 변경
//    @Test
//    void updateTest() {
//        Category category = Category.builder()
//                .id(1)
//                .name("수정된카테고리")
//                .type(CategoryType.FOOD)
//                .build();
//        int result = categoryMapper.update(category);
//        log.info("update 결과: " + result);
//    }

    /* 카테고리 - 삭제 */
//    @Test
//    void deleteTest() {
//        int result = categoryMapper.delete(1);
//        log.info("delete 결과: " + result);
//    }
}