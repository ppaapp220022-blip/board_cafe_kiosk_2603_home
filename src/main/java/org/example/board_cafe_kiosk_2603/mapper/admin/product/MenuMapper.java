package org.example.board_cafe_kiosk_2603.mapper.admin.product;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.product.Menu;
import org.example.board_cafe_kiosk_2603.dto.admin.product.MenuResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;

import java.util.List;
import java.util.Optional;


@Mapper

/*
 * 작성자 : 서주연
 * 기능 : 메뉴 상품 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface MenuMapper {
    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<MenuResponseDTO> findAll();

    /*
     * 작성자 : 서주연
     * 기능 : 카테고리 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    List<MenuResponseDTO> findByCategoryId(int categoryId);

    /*
     * 작성자 : 서주연
     * 기능 : 유형 기준 조회
     * 날짜 : 2026-03-27
     */
    List<MenuResponseDTO> findByType(String type);

    /*
     * 작성자 : 서주연
     * 기능 : 판매 가능 여부 기준 조회
     * 날짜 : 2026-03-30
     */
    List<MenuResponseDTO> findByIsAvailable(boolean isAvailable);

    /*
     * 작성자 : 서민성
     * 기능 : 삭제 상태 기준 조회
     * 날짜 : 2026-04-13
     */
    List<MenuResponseDTO> findByIsDeleted(boolean isDeleted);

    /*
     * 작성자 : 서민성
     * 기능 : 삭제 포함 ID 단건 조회
     * 날짜 : 2026-04-13
     */
    Optional<MenuResponseDTO> findByIdIncludeDeleted(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-13
     */
    int insert(Menu menu);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 수정
     * 날짜 : 2026-04-15
     */
    int update(Menu menu);

    /*
     * 작성자 : 서주연
     * 기능 : 소프트 삭제 처리
     * 날짜 : 2026-04-15
     */
    int softDelete(int id);

    /*
     * 작성자 : 김민기
     * 기능 : 삭제 데이터 복구
     * 날짜 : 2026-04-14
     */
    int restore(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 판매 가능 여부 전환
     * 날짜 : 2026-04-29
     */
    int toggleAvailable(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 유형별 목록 페이징 조회
     * 날짜 : 2026-04-29
     */
    List<MenuResponseDTO> findByTypePaged(@Param("type") String type,
                                          @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 유형별 건수 조회
     * 날짜 : 2026-04-13
     */
    int countByType(String type);

    /*
     * 작성자 : 서민성
     * 기능 : 유형 및 카테고리별 목록 페이징 조회
     * 날짜 : 2026-04-13
     */
    List<MenuResponseDTO> findByTypeAndCategoryIdPaged(@Param("type") String type,
                                                       @Param("categoryId") int categoryId,
                                                       @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 유형 및 카테고리별 건수 조회
     * 날짜 : 2026-04-13
     */
    int countByTypeAndCategoryId(@Param("type") String type, @Param("categoryId") int categoryId);

    /*
     * 작성자 : 서민성
     * 기능 : 삭제 상태별 목록 페이징 조회
     * 날짜 : 2026-04-13
     */
    List<MenuResponseDTO> findByIsDeletedPaged(@Param("isDeleted") boolean isDeleted,
                                               @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 삭제 상태별 건수 조회
     * 날짜 : 2026-04-13
     */
    int countByIsDeleted(boolean isDeleted);

    /*
     * 작성자 : 서주연
     * 기능 : 삭제 상태 및 카테고리별 목록 페이징 조회
     * 날짜 : 2026-04-15
     */
    List<MenuResponseDTO> findByIsDeletedAndCategoryIdPaged(@Param("isDeleted") boolean isDeleted,
                                                            @Param("categoryId") int categoryId,
                                                            @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 삭제 상태 및 카테고리별 건수 조회
     * 날짜 : 2026-04-15
     */
    int countByIsDeletedAndCategoryId(@Param("isDeleted") boolean isDeleted,
                                      @Param("categoryId") int categoryId);

    /*
     * 작성자 : 김민기
     * 기능 : 게임 메뉴가 없으면 등록
     * 날짜 : 2026-04-15
     */
    int insertGameMenuIfNotExists(@Param("categoryId") Integer categoryId,
                                  @Param("name") String name,
                                  @Param("description") String description);

    /*
     * 작성자 : 김민기
     * 기능 : 게임 메뉴 설명 수정
     * 날짜 : 2026-04-15
     */
    int updateGameMenuDescriptionByName(@Param("name") String name,
                                        @Param("description") String description);

    /*
     * 작성자 : 김민기
     * 기능 : 게임 메뉴명 변경
     * 날짜 : 2026-04-15
     */
    int renameGameMenuName(@Param("oldName") String oldName,
                           @Param("newName") String newName);

    /*
     * 작성자 : 서주연
     * 기능 : 게임명으로 메뉴 ID 조회
     * 날짜 : 2026-04-29
     */
    Integer findMenuIdByGameName(String gameName);

    /*
     * 작성자 : 서주연
     * 기능 : 게임 ID로 메뉴 ID 조회
     * 날짜 : 2026-04-29
     */
    Integer findMenuIdByGameId(int gameId);
}

