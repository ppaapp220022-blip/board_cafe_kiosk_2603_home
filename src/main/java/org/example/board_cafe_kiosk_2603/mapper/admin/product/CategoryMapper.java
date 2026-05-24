package org.example.board_cafe_kiosk_2603.mapper.admin.product;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.product.Category;
import org.example.board_cafe_kiosk_2603.domain.admin.product.CategoryType;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;

import java.util.List;
import java.util.Optional;

@Mapper

/*
 * 작성자 : 서주연
 * 기능 : 카테고리 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface CategoryMapper {
    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<CategoryResponseDTO> findAll();
    /*
     * 작성자 : 서주연
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-03-27
     */
    Optional<CategoryResponseDTO> findById(int id);
    /*
     * 작성자 : 서주연
     * 기능 : 타입별 목록 조회
     * 날짜 : 2026-03-27
     */
    List<CategoryResponseDTO> findByType(CategoryType type);
    /*
     * 작성자 : 서주연
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-27
     */
    int insert(Category category);
    /*
     * 작성자 : 서주연
     * 기능 : 데이터 수정
     * 날짜 : 2026-03-27
     */
    int update(@Param("id") int id, @Param("dto") CategoryRequestDTO dto);
    /*
     * 작성자 : 서주연
     * 기능 : 데이터 삭제
     * 날짜 : 2026-03-27
     */
    int delete(int id);
    /*
     * 작성자 : 서주연
     * 기능 : 연결된 상품 건수 조회
     * 날짜 : 2026-03-27
     */
    int countLinkedProducts(int id);
    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 페이징 조회
     * 날짜 : 2026-03-27
     */
    List<CategoryResponseDTO> findAllPaged(PageRequestDTO pageRequestDTO);
    /*
     * 작성자 : 서주연
     * 기능 : 전체 건수 조회
     * 날짜 : 2026-03-27
     */
    int countAll();
}
