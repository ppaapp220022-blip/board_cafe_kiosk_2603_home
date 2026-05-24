package org.example.board_cafe_kiosk_2603.service.admin.product;

import org.example.board_cafe_kiosk_2603.domain.admin.product.CategoryType;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;

import java.util.List;



/*
 * 작성자 : 서주연
 * 기능 : 카테고리 서비스 인터페이스
 * 날짜 : 2026-03-27
 */
public interface CategoryService {
    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<CategoryResponseDTO> getAll();
    /*
     * 작성자 : 서주연
     * 기능 : 유형별 목록 조회
     * 날짜 : 2026-03-27
     */
    List<CategoryResponseDTO> getByType(CategoryType type);
    /*
     * 작성자 : 서주연
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-03-27
     */
    CategoryResponseDTO getById(int id);
    /*
     * 작성자 : 서주연
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-27
     */
    void register(CategoryRequestDTO categoryRequestDTO);
    /*
     * 작성자 : 서주연
     * 기능 : 데이터 수정
     * 날짜 : 2026-03-27
     */
    void modify(int id, CategoryRequestDTO categoryRequestDTO);
    /*
     * 작성자 : 서주연
     * 기능 : 데이터 삭제
     * 날짜 : 2026-03-27
     */
    void remove(int id);
    /*
     * 작성자 : 서주연
     * 기능 : 삭제 가능 여부 확인
     * 날짜 : 2026-03-27
     */
    boolean canDelete(int id);
    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    PageResponseDTO<CategoryResponseDTO> getAll(PageRequestDTO pageRequestDTO);
}
