package org.example.board_cafe_kiosk_2603.service.admin.product;

import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItemStatus;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemResponseDTO;

import java.util.List;
import java.util.Map;


/*
 * 작성자 : 서주연
 * 기능 : 보드게임 재고 서비스 인터페이스
 * 날짜 : 2026-03-27
 */
public interface GameItemService {
    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<GameItemResponseDTO> getAll();

    /*
     * 작성자 : 서주연
     * 기능 : 게임 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    List<GameItemResponseDTO> getByGameId(int gameId);

    /*
     * 작성자 : 서주연
     * 기능 : 상태 기준 조회
     * 날짜 : 2026-03-27
     */
    List<GameItemResponseDTO> getByStatus(GameItemStatus gameItemStatus);

    /*
     * 작성자 : 김민기
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-04-14
     */
    GameItemResponseDTO getById(int id);

    /*
     * 작성자 : 김민기
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-14
     */
    void register(GameItemRequestDTO gameItemRequestDTO);

    /*
     * 작성자 : 김민기
     * 기능 : 데이터 수정
     * 날짜 : 2026-04-14
     */
    void modify(int id, GameItemRequestDTO gameItemRequestDTO);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 삭제
     * 날짜 : 2026-03-27
     */
    void remove(int id);

    /*
     * 작성자 : 서주연
     * 기능 : changeStatus 처리
     * 날짜 : 2026-03-27
     */
    void changeStatus(int id, GameItemStatus gameItemStatus);

    /*
     * 작성자 : 김민기
     * 기능 : 게임명 기준 대여 가능 재고 조회
     * 날짜 : 2026-04-15
     */
    List<GameItemResponseDTO> getAvailableByGameName(String gameName);

    /*
     * 작성자 : 김민기
     * 기능 : 주문에 게임 재고 할당
     * 날짜 : 2026-04-15
     */
    void assignGameItemsToOrder(int tableId, int orderId, String gameName, List<Integer> gameItemIds);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블의 활성 대여 게임 조회
     * 날짜 : 2026-04-15
     */
    List<Map<String, Object>> getActiveGameRentalsByTable(int tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 테이블의 게임 대여 이력 조회
     * 날짜 : 2026-04-15
     */
    List<Map<String, Object>> getGameRentalHistoryByTable(int tableId);

    /*
     * 작성자 : 김민기
     * 기능 : 게임 대여 정산 처리
     * 날짜 : 2026-04-15
     */
    void settleGameRentals(int tableId, List<Map<String, Object>> updates);
}

