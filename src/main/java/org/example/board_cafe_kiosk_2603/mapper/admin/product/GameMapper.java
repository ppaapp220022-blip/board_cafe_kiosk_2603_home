package org.example.board_cafe_kiosk_2603.mapper.admin.product;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.product.Game;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;

import java.util.List;
import java.util.Optional;

@Mapper

/*
 * 작성자 : 서주연
 * 기능 : 보드게임 상품 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface GameMapper {

    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<GameResponseDTO> findAll();

    /*
     * 작성자 : 김민기
     * 기능 : 카테고리 ID 기준 조회
     * 날짜 : 2026-04-14
     */
    List<GameResponseDTO> findByCategoryId(int categoryId);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태 기준 조회
     * 날짜 : 2026-03-27
     */
    List<GameResponseDTO> findByIsActive(boolean isActive);

    /*
     * 작성자 : 서민성
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-04-13
     */
    Optional<GameResponseDTO> findById(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 이름 목록 기준 조회
     * 날짜 : 2026-04-13
     */
    List<GameResponseDTO> findByNames(@Param("names") List<String> names);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-16
     */
    int insert(Game game);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 수정
     * 날짜 : 2026-04-13
     */
    int update(Game game);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 삭제
     * 날짜 : 2026-03-27
     */
    int delete(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태 전환
     * 날짜 : 2026-03-27
     */
    int toggleActive(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 전체 목록 페이징 조회
     * 날짜 : 2026-04-13
     */
    List<GameResponseDTO> findAllPaged(PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 전체 건수 조회
     * 날짜 : 2026-04-13
     */
    int countAll();

    /*
     * 작성자 : 서민성
     * 기능 : 카테고리별 목록 페이징 조회
     * 날짜 : 2026-04-13
     */
    List<GameResponseDTO> findByCategoryIdPaged(@Param("categoryId") int categoryId,
                                                @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태별 목록 페이징 조회
     * 날짜 : 2026-04-16
     */
    List<GameResponseDTO> findByIsActivePaged(@Param("isActive") boolean isActive,
                                              @Param("categoryId") Integer categoryId,
                                              @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태별 건수 조회
     * 날짜 : 2026-04-16
     */
    int countByIsActive(@Param("isActive") boolean isActive,
                        @Param("categoryId") Integer categoryId);

    /*
     * 작성자 : 서민성
     * 기능 : 카테고리별 건수 조회
     * 날짜 : 2026-04-13
     */
    int countByCategoryId(int categoryId);
}

