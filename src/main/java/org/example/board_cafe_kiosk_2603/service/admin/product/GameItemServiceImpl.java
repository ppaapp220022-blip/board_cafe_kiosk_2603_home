package org.example.board_cafe_kiosk_2603.service.admin.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItem;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItemStatus;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemResponseDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.table.CafeTableMapper;
import org.example.board_cafe_kiosk_2603.mapper.admin.product.GameItemMapper;
import org.example.board_cafe_kiosk_2603.service.kiosk.order.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * GameItemService 구현체
 * ModelMapper를 사용하여 Domain ↔ DTO 변환 처리
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class GameItemServiceImpl implements GameItemService  {

    private final GameItemMapper gameItemMapper;
    private final CafeTableMapper cafeTableMapper;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    /**
     * 전체 게임 아이템 목록 조회
     */
    @Override
    public List<GameItemResponseDTO> getAll() {
        log.debug("GameItemServiceImpl.getAll() 실행");
        List<GameItemResponseDTO> list = gameItemMapper.findAll();
        log.debug("조회된 게임 아이템 수: {}", list.size());
        return list;
    }

    /**
     * game_id 기준 게임 아이템 목록 조회
     */
    @Override
    public List<GameItemResponseDTO> getByGameId(int gameId) {
        log.debug("GameItemServiceImpl.getByGameId() 실행 - gameId: {}", gameId);
        List<GameItemResponseDTO> list = gameItemMapper.findByGameId(gameId);
        log.debug("조회된 게임 아이템 수 (gameId={}): {}", gameId, list.size());
        return list;
    }

    /**
     * status 기준 게임 아이템 목록 조회
     */
    @Override
    public List<GameItemResponseDTO> getByStatus(GameItemStatus gameItemStatus) {
        log.debug("GameItemServiceImpl.getByStatus() 실행 - gameItemStatus: {}", gameItemStatus);
        List<GameItemResponseDTO> list = gameItemMapper.findByStatus(gameItemStatus);
        log.debug("조회된 게임 아이템 수 (status={}): {}", gameItemStatus, list.size());
        return list;
    }

    /**
     * PK로 게임 아이템 단건 조회
     */
    @Override
    public GameItemResponseDTO getById(int id) {
        log.debug("GameItemServiceImpl.getById() 실행 - id: {}", id);
        return gameItemMapper.findById(id)
                .orElseThrow(() -> {
                    log.warn("게임 아이템 없음 - id: {}", id);
                    return new NoSuchElementException("게임 아이템을 찾을 수 없습니다. id=" + id);
                });
    }

    /**
     * 게임 아이템 등록
     */
    @Override
    public void register(GameItemRequestDTO gameItemRequestDTO) {
        log.debug("GameItemServiceImpl.register() 실행 - gameItemRequestDTO: {}", gameItemRequestDTO);
        GameItem gameItem = modelMapper.map(gameItemRequestDTO, GameItem.class);
        int result = gameItemMapper.insert(gameItem);
        log.debug("게임 아이템 등록 결과 - affected rows: {}, generated id: {}", result, gameItem.getId());
    }

    /**
     * 게임 아이템 수정 (존재 여부 선확인)
     */
    @Override
    public void modify(int id, GameItemRequestDTO gameItemRequestDTO) {
        log.debug("GameItemServiceImpl.modify() 실행 - id: {}, dto: {}", id, gameItemRequestDTO);
        gameItemMapper.findById(id)
                .orElseThrow(() -> {
                    log.warn("수정 대상 게임 아이템 없음 - id: {}", id);
                    return new NoSuchElementException("게임 아이템을 찾을 수 없습니다. id=" + id);
                });
        GameItem gameItem = GameItem.builder()
                .id(id)
                .gameId(gameItemRequestDTO.getGameId())
                .serialNumber(gameItemRequestDTO.getSerialNumber())
                .status(gameItemRequestDTO.getStatus())
                .build();
        int result = gameItemMapper.update(gameItem);
        log.debug("게임 아이템 수정 결과 - affected rows: {}", result);
    }

    /**
     * 게임 아이템 삭제 (존재 여부 선확인)
     */
    @Override
    public void remove(int id) {
        log.debug("GameItemServiceImpl.remove() 실행 - id: {}", id);
        GameItemResponseDTO gameItem = gameItemMapper.findById(id)
                .orElseThrow(() -> {
                    log.warn("삭제 대상 게임 아이템 없음 - id: {}", id);
                    return new NoSuchElementException("게임 아이템을 찾을 수 없습니다. id=" + id);
                });

        if (gameItem.getStatus() == GameItemStatus.NORMAL || gameItem.getStatus() == GameItemStatus.RENTED) {
            String message = (gameItem.getStatus() == GameItemStatus.NORMAL)
                    ? "대여 가능 상태(NORMAL) 아이템은 삭제할 수 없습니다."
                    : "대여 중 상태(RENTED) 아이템은 삭제할 수 없습니다.";
            log.warn("게임 아이템 삭제 정책 위반 - id: {}, status: {}", id, gameItem.getStatus());
            throw new IllegalStateException(message);
        }

        int result = gameItemMapper.delete(id);
        log.debug("게임 아이템 삭제 결과 - affected rows: {}", result);
    }

    /**
     * 게임 아이템 상태 변경 (존재 여부 선확인)
     */
    @Override
    public void changeStatus(int id, GameItemStatus status) {
        log.debug("GameItemServiceImpl.changeStatus() 실행 - id: {}, status: {}", id, status);
        gameItemMapper.findById(id)
                .orElseThrow(() -> {
                    log.warn("상태 변경 대상 게임 아이템 없음 - id: {}", id);
                    return new NoSuchElementException("게임 아이템을 찾을 수 없습니다. id=" + id);
                });
        int result = gameItemMapper.updateStatus(id, status);
        log.debug("게임 아이템 상태 변경 결과 - affected rows: {}", result);
    }

    @Override
    public List<GameItemResponseDTO> getAvailableByGameName(String gameName) {
        String normalized = (gameName == null) ? "" : gameName.trim();
        if (normalized.isEmpty()) {
            return List.of();
        }

        return gameItemMapper.findAll().stream()
                .filter(item -> normalized.equals(item.getGameName()))
                .filter(item -> item.getStatus() == GameItemStatus.NORMAL)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignGameItemsToOrder(int tableId, int orderId, String gameName, List<Integer> gameItemIds) {
        String normalized = (gameName == null) ? "" : gameName.trim();
        if (tableId <= 0 || orderId <= 0 || normalized.isEmpty() || gameItemIds == null || gameItemIds.isEmpty()) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        Long sessionId = resolveSessionId(tableId);

        int orderMatch = gameItemMapper.countOrderItemInSession(orderId, sessionId, normalized);
        if (orderMatch <= 0) {
            throw new IllegalArgumentException("현재 테이블 세션의 주문 항목과 일치하지 않습니다.");
        }

        for (Integer itemId : gameItemIds) {
            if (itemId == null || itemId <= 0) continue;

            GameItemResponseDTO item = getById(itemId);
            if (!normalized.equals(item.getGameName())) {
                throw new IllegalArgumentException("선택한 시리얼이 주문 게임과 일치하지 않습니다. id=" + itemId);
            }
            if (item.getStatus() != GameItemStatus.NORMAL) {
                throw new IllegalStateException("대여 가능한 상태가 아닙니다. id=" + itemId);
            }

            changeStatus(itemId, GameItemStatus.RENTED);
            gameItemMapper.insertGameHistory(sessionId, itemId);
        }

        // 게임 주문은 일련번호가 배정되면 주문 확인 단계로 전환한다.
        var updateResult = orderService.updateStatus(orderId, "CONFIRMED");
        if (!updateResult.isSuccess()) {
            log.warn("게임 일련번호 배정 후 주문 상태 변경 실패 - orderId: {}, reason: {}",
                    orderId, updateResult.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getActiveGameRentalsByTable(int tableId) {
        Long sessionId = resolveSessionId(tableId);
        return gameItemMapper.findActiveGameRentalsBySessionId(sessionId);
    }

    @Override
    public List<Map<String, Object>> getGameRentalHistoryByTable(int tableId) {
        Long sessionId = resolveSessionId(tableId);
        return gameItemMapper.findGameRentalHistoryBySessionId(sessionId);
    }

    @Override
    @Transactional
    public void settleGameRentals(int tableId, List<Map<String, Object>> updates) {
        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("반납 처리할 데이터가 없습니다.");
        }
        Long sessionId = resolveSessionId(tableId);

        for (Map<String, Object> update : updates) {
            long historyId = Long.parseLong(String.valueOf(update.get("historyId")));
            int gameItemId = Integer.parseInt(String.valueOf(update.get("gameItemId")));
            String statusText = String.valueOf(update.get("status")).trim().toUpperCase();

            if (!List.of("NORMAL", "DAMAGED", "LOST").contains(statusText)) {
                throw new IllegalArgumentException("지원하지 않는 반납 상태입니다: " + statusText);
            }

            Map<String, Object> historyRow = gameItemMapper.findGameHistoryById(historyId);

            if (historyRow == null) {
                throw new NoSuchElementException("game_history가 존재하지 않습니다. id=" + historyId);
            }

            long rowSessionId = ((Number) historyRow.get("sessionId")).longValue();
            int rowGameItemId = ((Number) historyRow.get("gameItemId")).intValue();
            String rowStatus = String.valueOf(historyRow.get("status"));

            if (rowSessionId != sessionId || rowGameItemId != gameItemId) {
                throw new IllegalArgumentException("현재 테이블 세션과 일치하지 않는 대여 이력입니다. historyId=" + historyId);
            }
            if (!"RENTED".equalsIgnoreCase(rowStatus)) {
                throw new IllegalStateException("이미 반납 완료된 이력입니다. historyId=" + historyId);
            }

            GameItemStatus itemStatus = switch (statusText) {
                case "NORMAL" -> GameItemStatus.NORMAL;
                case "DAMAGED" -> GameItemStatus.DAMAGED;
                case "LOST" -> GameItemStatus.LOST;
                default -> throw new IllegalArgumentException("지원하지 않는 상태: " + statusText);
            };

            changeStatus(gameItemId, itemStatus);
            gameItemMapper.updateGameHistoryStatus(historyId, statusText);
        }
    }

    private Long resolveSessionId(int tableId) {
        Long sessionId = cafeTableMapper.selectCurrentSessionId(tableId);
        if (sessionId == null) {
            sessionId = cafeTableMapper.selectActiveSessionByTableId(tableId);
        }
        if (sessionId == null) {
            sessionId = cafeTableMapper.selectLatestSessionByTableId(tableId);
        }
        if (sessionId == null) {
            throw new IllegalStateException("활성 세션을 찾을 수 없습니다. tableId=" + tableId);
        }
        return sessionId;
    }
}
