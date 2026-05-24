package org.example.board_cafe_kiosk_2603.service.admin.product;

import org.example.board_cafe_kiosk_2603.dto.admin.product.MenuRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.MenuResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;

import java.util.List;


/*
 * 작성자 : 서주연
 * 기능 : 메뉴 상품 서비스 인터페이스
 * 날짜 : 2026-03-27
 */
public interface MenuService {
    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<MenuResponseDTO> getAll();

    /*
     * 작성자 : 서주연
     * 기능 : 카테고리 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    List<MenuResponseDTO> getByCategoryId(int categoryId);

    /*
     * 작성자 : 서주연
     * 기능 : 판매 가능 여부별 조회
     * 날짜 : 2026-03-27
     */
    List<MenuResponseDTO> getByIsAvailable(boolean isAvailable);

    /*
     * 작성자 : 서주연
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-03-30
     */
    MenuResponseDTO getById(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-30
     */
    void register(MenuRequestDTO menuRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 수정
     * 날짜 : 2026-04-13
     */
    void modify(int id, MenuRequestDTO menuRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 삭제
     * 날짜 : 2026-04-15
     */
    void remove(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 삭제 데이터 복구
     * 날짜 : 2026-03-30
     */
    void restore(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 판매 가능 여부 전환
     * 날짜 : 2026-03-27
     */
    void toggleAvailable(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 유형별 목록 조회
     * 날짜 : 2026-04-13
     */
    List<MenuResponseDTO> getByType(String type);

    /*
     * 작성자 : 서민성
     * 기능 : 삭제 상태별 조회
     * 날짜 : 2026-04-13
     */
    List<MenuResponseDTO> getByIsDeleted(boolean isDeleted);

    /*
     * 작성자 : 서민성
     * 기능 : 유형별 목록 조회
     * 날짜 : 2026-04-13
     */
    PageResponseDTO<MenuResponseDTO> getByType(String type, PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 유형 및 카테고리별 조회
     * 날짜 : 2026-04-13
     */
    PageResponseDTO<MenuResponseDTO> getByTypeAndCategoryId(String type, int categoryId, PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 삭제 상태별 조회
     * 날짜 : 2026-04-13
     */
    PageResponseDTO<MenuResponseDTO> getByIsDeleted(boolean isDeleted, PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 삭제 상태 및 카테고리별 조회
     * 날짜 : 2026-04-15
     */
    PageResponseDTO<MenuResponseDTO> getByIsDeletedAndCategoryId(boolean isDeleted, int categoryId, PageRequestDTO pageRequestDTO);
}

