package org.example.board_cafe_kiosk_2603.mapper.admin.product;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItem;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItemStatus;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper

/*
 * 작성자 : 서주연
 * 기능 : 보드게임 개별 재고 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface GameItemMapper {

    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<GameItemResponseDTO> findAll();

    /*
     * 작성자 : 서주연
     * 기능 : 게임 ID 기준 조회
     * 날짜 : 2026-03-27
     */
    List<GameItemResponseDTO> findByGameId(int gameId);

    /*
     * 작성자 : 서주연
     * 기능 : 상태 기준 조회
     * 날짜 : 2026-03-27
     */
    List<GameItemResponseDTO> findByStatus(@Param("status") GameItemStatus gameItemStatus);

    /*
     * 작성자 : 서주연
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-03-27
     */
    Optional<GameItemResponseDTO> findById(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-27
     */
    int insert(GameItem gameItem);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 수정
     * 날짜 : 2026-03-27
     */
    int update(GameItem gameItem);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 삭제
     * 날짜 : 2026-03-27
     */
    int delete(int id);

    /*
     * 작성자 : 서주연
     * 기능 : 상태 변경
     * 날짜 : 2026-03-27
     */
    int updateStatus(@Param("id") int id, @Param("status") GameItemStatus gameItemStatus);

    /*
     * 작성자 : 서주연
     * 기능 : 건수 조회
     * 날짜 : 2026-03-27
     */
    int countOrderItemInSession(@Param("orderId") int orderId,
                                @Param("sessionId") long sessionId,
                                @Param("menuName") String menuName);

    /*
     * 작성자 : 서주연
     * 기능 : 건수 조회
     * 날짜 : 2026-03-27
     */
    int countMatchedRentedGameHistoriesForOrder(@Param("orderId") int orderId,
                                                @Param("sessionId") long sessionId);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-27
     */
    int insertGameHistory(@Param("sessionId") long sessionId,
                          @Param("gameItemId") int gameItemId);

    /*
     * 작성자 : 서주연
     * 기능 : findActiveGameRentalsBySessionId 처리
     * 날짜 : 2026-03-27
     */
    List<Map<String, Object>> findActiveGameRentalsBySessionId(@Param("sessionId") long sessionId);

    /*
     * 작성자 : 서주연
     * 기능 : findGameRentalHistoryBySessionId 처리
     * 날짜 : 2026-03-27
     */
    List<Map<String, Object>> findGameRentalHistoryBySessionId(@Param("sessionId") long sessionId);

    /*
     * 작성자 : 서주연
     * 기능 : findGameHistoryById 처리
     * 날짜 : 2026-03-27
     */
    Map<String, Object> findGameHistoryById(@Param("historyId") long historyId);

    /*
     * 작성자 : 서주연
     * 기능 : 데이터 수정
     * 날짜 : 2026-03-27
     */
    int updateGameHistoryStatus(@Param("historyId") long historyId,
                                @Param("status") String status);

    /*
     * 작성자 : 서주연
     * 기능 : returnActiveRentalsBySessionId 처리
     * 날짜 : 2026-03-27
     */
    int returnActiveRentalsBySessionId(@Param("sessionId") long sessionId);

    /*
     * 작성자 : 서주연
     * 기능 : normalizeNormalItemsBySessionId 처리
     * 날짜 : 2026-03-27
     */
    int normalizeNormalItemsBySessionId(@Param("sessionId") long sessionId);
}

