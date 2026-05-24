package org.example.board_cafe_kiosk_2603.service.admin.product;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.product.CategoryType;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 작성자 : 서주연
 * 기능 : CategoryServiceImpl 테스트
 * 날짜 : 2026-03-30
 */

@Log4j2
@SpringBootTest
@Transactional
class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    private CategoryResponseDTO createCategory(String name, CategoryType type) {
        CategoryRequestDTO request = CategoryRequestDTO.builder()
                .name(name)
                .type(type)
                .build();
        categoryService.register(request);
        return categoryService.getAll().stream()
                .filter(category -> name.equals(category.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("생성한 카테고리를 찾지 못했습니다."));
    }

    @Test
    void getAllTest() {
        List<CategoryResponseDTO> list = categoryService.getAll();
        list.forEach(category -> log.info(category));
    }

    @Test
    void getByTypeTest() {
        List<CategoryResponseDTO> list = categoryService.getByType(CategoryType.GAME);
        list.forEach(category -> log.info(category));
    }

    @Test
    void getByIdTest() {
        CategoryResponseDTO category = categoryService.getById(1);
        log.info(category);
    }

    @Test
    void registerTest() {
        CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder()
                .name("테스트카테고리_" + System.currentTimeMillis())
                .type(CategoryType.GAME)
                .build();
        categoryService.register(categoryRequestDTO);
        log.info("register 완료");
    }

    @Test
    void modifyTest() {
        CategoryResponseDTO created = createCategory("수정전카테고리_" + System.currentTimeMillis(), CategoryType.FOOD);
        CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder()
                .name("수정된카테고리")
                .type(CategoryType.FOOD)
                .build();
        categoryService.modify(created.getId(), categoryRequestDTO);
        log.info("modify 완료");
    }

    @Test
    void removeTest() {
        CategoryResponseDTO created = createCategory("삭제용카테고리_" + System.currentTimeMillis(), CategoryType.GAME);
        categoryService.remove(created.getId());
        log.info("remove 완료");
    }
}
