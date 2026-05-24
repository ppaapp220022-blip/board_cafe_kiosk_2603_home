package org.example.board_cafe_kiosk_2603.service.admin.product;


import org.example.board_cafe_kiosk_2603.dto.admin.product.GameRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;

import java.util.List;


/*
 * 작성자 : 서주연
 * 기능 : 보드게임 상품 서비스 인터페이스
 * 날짜 : 2026-03-27
 */
public interface GameService {
    /*
     * 작성자 : 서민성
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-04-13
     */
    List<GameResponseDTO> getAll();

    /*
     * 작성자 : 서민성
     * 기능 : 카테고리 ID 기준 조회
     * 날짜 : 2026-04-13
     */
    List<GameResponseDTO> getByCategoryId(int categoryId);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태별 조회
     * 날짜 : 2026-04-16
     */
    List<GameResponseDTO> getByIsActive(boolean isActive);

    /*
     * 작성자 : 서주연
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-03-27
     */
    GameResponseDTO getById(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 이름 목록 기준 조회
     * 날짜 : 2026-04-13
     */
    List<GameResponseDTO> getByNames(List<String> names);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-30
     */
    int register(GameRequestDTO gameRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 수정
     * 날짜 : 2026-03-27
     */
    void modify(int id, GameRequestDTO gameRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 삭제
     * 날짜 : 2026-03-27
     */
    void remove(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태 전환
     * 날짜 : 2026-03-27
     */
    void toggleActive(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-04-13
     */
    PageResponseDTO<GameResponseDTO> getAll(PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 카테고리 ID 기준 조회
     * 날짜 : 2026-04-13
     */
    PageResponseDTO<GameResponseDTO> getByCategoryId(int categoryId, PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태별 조회
     * 날짜 : 2026-04-16
     */
    PageResponseDTO<GameResponseDTO> getByIsActive(boolean isActive, Integer categoryId, PageRequestDTO pageRequestDTO);
}

