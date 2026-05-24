package org.example.board_cafe_kiosk_2603.service.admin.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.ai.GameEmbeddingService;
import org.example.board_cafe_kiosk_2603.domain.admin.product.Menu;
import org.example.board_cafe_kiosk_2603.dto.admin.product.MenuRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.MenuResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.product.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/*
 * 작성자 : 서주연
 * 기능 : Menu 관련 비즈니스 로직을 처리하는 서비스 구현체
 * 날짜 : 2026-03-27
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;
    private final GameEmbeddingService gameEmbeddingService;
    /*
     * 작성자 : 서주연
     * 기능 : 전체 메뉴 목록 조회 (소프트 삭제 제외)
     * 날짜 : 2026-03-27
     */

    @Override
    public List<MenuResponseDTO> getAll() {
        log.debug("MenuServiceImpl.getAll() 실행");
        List<MenuResponseDTO> list = menuMapper.findAll();
        log.debug("조회된 메뉴 수: {}", list.size());
        return list;
    }
    /*
     * 작성자 : 서주연
     * 기능 : category_id 기준 메뉴 목록 조회
     * 날짜 : 2026-03-27
     */

    @Override
    public List<MenuResponseDTO> getByCategoryId(int categoryId) {
        log.debug("MenuServiceImpl.getByCategoryId() 실행 - categoryId: {}", categoryId);
        List<MenuResponseDTO> list = menuMapper.findByCategoryId(categoryId);
        log.debug("조회된 메뉴 수 (categoryId={}): {}", categoryId, list.size());
        return list;
    }
    /*
     * 작성자 : 서주연
     * 기능 : category type 기준 메뉴 목록 조회 (FOOD / DRINK / GUEST)
     * 날짜 : 2026-03-27
     */

    @Override
    public List<MenuResponseDTO> getByType(String type) {
        log.debug("MenuServiceImpl.getByType() 실행 - type: {}", type);
        List<MenuResponseDTO> list = menuMapper.findByType(type);
        log.debug("조회된 메뉴 수 (type={}): {}", type, list.size());
        return list;
    }
    /*
     * 작성자 : 서주연
     * 기능 : 소프트 삭제 여부 기준 메뉴 목록 조회 (숨김 탭용)
     * 날짜 : 2026-03-30
     */

    @Override
    public List<MenuResponseDTO> getByIsDeleted(boolean isDeleted) {
        log.debug("MenuServiceImpl.getByIsDeleted() 실행 - isDeleted: {}", isDeleted);
        List<MenuResponseDTO> list = menuMapper.findByIsDeleted(isDeleted);
        log.debug("조회된 메뉴 수 (isDeleted={}): {}", isDeleted, list.size());
        return list;
    }
    /*
     * 작성자 : 서주연
     * 기능 : 판매 가능 여부 기준 메뉴 목록 조회
     * 날짜 : 2026-03-27
     */

    @Override
    public List<MenuResponseDTO> getByIsAvailable(boolean isAvailable) {
        log.debug("MenuServiceImpl.getByIsAvailable() 실행 - isAvailable: {}", isAvailable);
        List<MenuResponseDTO> list = menuMapper.findByIsAvailable(isAvailable);
        log.debug("조회된 메뉴 수 (isAvailable={}): {}", isAvailable, list.size());
        return list;
    }
    /*
     * 작성자 : 서주연
     * 기능 : PK로 메뉴 단건 조회
     * 날짜 : 2026-03-27
     */

    @Override
    public MenuResponseDTO getById(int id) {
        log.debug("MenuServiceImpl.getById() 실행 - id: {}", id);
        return menuMapper.findByIdIncludeDeleted(id)
                .orElseThrow(() -> {
                    log.warn("메뉴 없음 - id: {}", id);
                    return new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id);
                });
    }
    /*
     * 작성자 : 서주연
     * 기능 : 메뉴 등록
     * 날짜 : 2026-03-27
     */

    @Override
    public void register(MenuRequestDTO dto) {
        log.debug("MenuServiceImpl.register() 실행 - dto: {}", dto);
        Menu menu = Menu.builder()
                .categoryId(dto.getCategoryId())
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .isAvailable(dto.isAvailable())
                .build();
        int result = menuMapper.insert(menu);
        log.debug("메뉴 등록 결과 - affected rows: {}, generated id: {}", result, menu.getId());
    }
    /*
     * 작성자 : 서주연
     * 기능 : 메뉴 수정
     * 날짜 : 2026-03-27
     */

    @Override
    public void modify(int id, MenuRequestDTO dto) {
        log.debug("MenuServiceImpl.modify() 실행 - id: {}, dto: {}", id, dto);

        // 1. 수정 전 기존 메뉴 조회 (GAME 타입 여부 확인용)
        MenuResponseDTO origin = menuMapper.findByIdIncludeDeleted(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));
        Menu menu = Menu.builder()
                .id(id)
                .categoryId(dto.getCategoryId())
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .isAvailable(dto.isAvailable())
                .build();
        int result = menuMapper.update(menu);

        // GAME 타입 메뉴인 경우에만 임베딩 갱신
        if (isGameCategory(origin.getCategoryType())) {
            tryUpsertEmbeddingByMenuId(id);
        }
        log.info("메뉴 수정 결과 - affected rows: {}", result);
    }
    /*
     * 작성자 : 서주연
     * 기능 : 메뉴 소프트 삭제 (is_deleted = true)
     * 날짜 : 2026-03-27
     */

    @Override
    public void remove(int id) {
        log.debug("MenuServiceImpl.remove() 실행 - id: {}", id);

        MenuResponseDTO menu = menuMapper.findByIdIncludeDeleted(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));

        menuMapper.softDelete(id);

        // GAME 타입 메뉴인 경우 임베딩 삭제
        if (isGameCategory(menu.getCategoryType())) {
            tryDeleteEmbeddingByMenuId(id);
        }
        log.debug("메뉴 소프트 삭제 결과 - affected rows: {}", id);
    }
    /*
     * 작성자 : 서주연
     * 기능 : 메뉴 복원 (is_deleted = false)
     * 날짜 : 2026-03-30
     */

    @Override
    public void restore(int id) {
        log.debug("MenuServiceImpl.restore() 실행 - id: {}", id);
        MenuResponseDTO menu = menuMapper.findByIdIncludeDeleted(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));

        menuMapper.restore(id);

        // GAME 타입 메뉴인 경우 임베딩 재등록 시도
        if (isGameCategory(menu.getCategoryType())) {
            tryUpsertEmbeddingByMenuId(id);
        }
        log.debug("메뉴 복원 결과 - affected rows: {}", id);
    }
    /*
     * 작성자 : 서주연
     * 기능 : 메뉴 판매 상태 토글
     * 날짜 : 2026-03-27
     */

    @Override
    public void toggleAvailable(int id) {
        log.debug("MenuServiceImpl.toggleAvailable() 실행 - id: {}", id);

        MenuResponseDTO menu = menuMapper.findByIdIncludeDeleted(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));

        menuMapper.toggleAvailable(id);

        // GAME 타입 메뉴인 경우 임베딩 상태 재확인
        if (isGameCategory(menu.getCategoryType())) {
            tryUpsertEmbeddingByMenuId(id);
        }
        log.debug("메뉴 판매 상태 토글 결과 - affected rows: {}", id);
    }
    /*
     * 작성자 : 서주연
     * 기능 : 카테고리 타입 판별 (GAME 여부)
     * 날짜 : 2026-03-27
     */

    private boolean isGameCategory(String categoryType) {
        return "GAME".equalsIgnoreCase(categoryType);
    }
    /*
     * 작성자 : 서주연
     * 기능 : AI 임베딩 갱신 트리거 (예외 격리)
     * 날짜 : 2026-03-27
     */

    private void tryUpsertEmbeddingByMenuId(int menuId) {
        try {
            gameEmbeddingService.upsertGameByMenuId(menuId);
        } catch (Exception e) {
            log.error("[임베딩] upsert 실패 - menuId={}, 원인={}", menuId, e.getMessage());
        }
    }
    /*
     * 작성자 : 서주연
     * 기능 : AI 임베딩 삭제 트리거 (예외 격리)
     * 날짜 : 2026-03-27
     */

    private void tryDeleteEmbeddingByMenuId(int menuId) {
        try {
            gameEmbeddingService.deleteByMenuId(menuId);
        } catch (Exception e) {
            log.error("[임베딩] delete 실패 - menuId={}, 원인={}", menuId, e.getMessage());
        }
    }
    /*
     * 작성자 : 서민성
     * 기능 : 유형별 목록 조회
     * 날짜 : 2026-04-13
     */

    @Override
    public PageResponseDTO<MenuResponseDTO> getByType(String type, PageRequestDTO pageRequestDTO) {
        log.debug("MenuServiceImpl.getByType(paged) 실행 - type: {}", type);
        List<MenuResponseDTO> list = menuMapper.findByTypePaged(type, pageRequestDTO);
        int total = menuMapper.countByType(type);
        log.debug("조회된 메뉴 수 (type={}): {}, 전체: {}", type, list.size(), total);
        return new PageResponseDTO<>(pageRequestDTO, total, list);
    }
    /*
     * 작성자 : 서민성
     * 기능 : category type + category_id 기준 메뉴 목록 조회 - 페이징
     * 날짜 : 2026-04-13
     */

    @Override
    public PageResponseDTO<MenuResponseDTO> getByTypeAndCategoryId(String type, int categoryId, PageRequestDTO pageRequestDTO) {
        log.debug("MenuServiceImpl.getByTypeAndCategoryId(paged) 실행 - type: {}, categoryId: {}", type, categoryId);
        List<MenuResponseDTO> list = menuMapper.findByTypeAndCategoryIdPaged(type, categoryId, pageRequestDTO);
        int total = menuMapper.countByTypeAndCategoryId(type, categoryId);
        log.debug("조회된 메뉴 수 (type={}, categoryId={}): {}, 전체: {}", type, categoryId, list.size(), total);
        return new PageResponseDTO<>(pageRequestDTO, total, list);
    }
    /*
     * 작성자 : 서민성
     * 기능 : 소프트 삭제 여부 기준 메뉴 목록 조회 - 페이징 (숨김 탭용)
     * 날짜 : 2026-04-13
     */

    @Override
    public PageResponseDTO<MenuResponseDTO> getByIsDeleted(boolean isDeleted, PageRequestDTO pageRequestDTO) {
        log.debug("MenuServiceImpl.getByIsDeleted(paged) 실행 - isDeleted: {}", isDeleted);
        List<MenuResponseDTO> list = menuMapper.findByIsDeletedPaged(isDeleted, pageRequestDTO);
        int total = menuMapper.countByIsDeleted(isDeleted);
        log.debug("조회된 메뉴 수 (isDeleted={}): {}, 전체: {}", isDeleted, list.size(), total);
        return new PageResponseDTO<>(pageRequestDTO, total, list);
    }

    /*
     * 작성자 : 서주연
     * 기능 : getByIsDeletedAndCategoryId 메서드
     * 날짜 : 2026-04-15
     */

    @Override
    public PageResponseDTO<MenuResponseDTO> getByIsDeletedAndCategoryId(boolean isDeleted, int categoryId, PageRequestDTO pageRequestDTO) {
        List<MenuResponseDTO> list = menuMapper.findByIsDeletedAndCategoryIdPaged(isDeleted, categoryId, pageRequestDTO);
        int total = menuMapper.countByIsDeletedAndCategoryId(isDeleted, categoryId);
        return PageResponseDTO.<MenuResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .total(total)
                .dtoList(list)
                .build();
    }
}
