package org.example.board_cafe_kiosk_2603.controller.admin.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItem;
import org.example.board_cafe_kiosk_2603.domain.admin.product.GameItemStatus;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameItemResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.GameResponseDTO;
import org.example.board_cafe_kiosk_2603.service.admin.product.GameItemService;
import org.example.board_cafe_kiosk_2603.service.admin.product.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/admin/product/game-items")
@RequiredArgsConstructor
public class GameItemController {
    /* 게임 아이템(재고) CRUD 컨트롤러 */
    // 재고 목록 조회
    // 개별 아이템 등록/수정/삭제
    // 아이템 상태(대여가능/수리중 등) 관리

    private final GameService gameService;
    private final GameItemService gameItemService;

    /**
     * 게임 아이템 전체 목록 조회 → 뷰 반환
     * GET /admin/product/game-items
     */
    /* 게임 아이템 전체 목록 조회 */
    @GetMapping
    public String getAll(Model model) {
        log.info("--- 게임 아이템 전체 목록 조회 요청 ---");
        try {
            List<GameItemResponseDTO> gameItemList = gameItemService.getAll();
            model.addAttribute("gameItemList", gameItemList);
            model.addAttribute("activePage", "productReg");
            log.debug("--- 게임 아이템 목록 조회 완료 - 건수: {} ---", gameItemList.size());
        } catch (Exception e) {
            log.error("--- 게임 아이템 목록 로드 중 오류 발생: {}", e.getMessage());
        }
        return "admin/product_game";
    }

    /**
     * 게임 아이템 등록 폼 페이지
     * GET /admin/product/game-items/add
     */
    /* 게임 아이템 등록 페이지 이동 */
//    @GetMapping("/add")
//    public String addForm(Model model) {
//        log.info("--- 게임 아이템 등록 폼 요청 ---");
//
//        // 아이템이 속할 게임 목록과 선택 가능한 상태 값(Enum) 전달
//        List<GameResponseDTO> gameList = gameService.getAll();
//        model.addAttribute("gameList", gameList);
//        model.addAttribute("statusList", GameItemStatus.values());
//        model.addAttribute("activePage", "productReg");
//
////        return "admin/product_game_item_form";
//        return "admin/product_game_form";
//    }

    /* 게임 아이템 등록 처리 */
//    @PostMapping("/add")
//    public String register(@ModelAttribute GameItemRequestDTO gameItemRequestDTO) {
//        log.info("---  게임 아이템 신규 등록 시작 - DTO: {} ---", gameItemRequestDTO);
//
//        gameItemService.register(gameItemRequestDTO);
//
//        log.info("--- 게임 아이템 등록 성공 ---");
////        return "redirect:/admin/product/game-items";
//        return "redirect:/admin/product/game";
//    }

    /* 게임 아이템 수정 폼 페이지 */
//    @GetMapping("/edit/{id}")
//    public String editForm(@PathVariable int id, Model model) {
//        log.info("--- 게임 아이템 수정 폼 요청 (ItemID: {}) ---", id);
//
//        GameItemResponseDTO gameItem = gameItemService.getById(id);
//        List<GameResponseDTO> gameList = gameService.getAll();
//
//        model.addAttribute("gameItem", gameItem);
//        model.addAttribute("gameList", gameList);
//        model.addAttribute("statusList", GameItemStatus.values());
//        model.addAttribute("activePage", "productReg");
//
//        log.info("--- 게임 아이템 수정 완료  ---");
////        return "admin/product_game_item_form";
//        return "admin/product_game_form";
//    }

    /**
     * 게임 아이템 수정 처리
     * POST /admin/product/game-items/edit/{id}
     */
    /* 게임 아이템 정보 수정 처리 */
    @PostMapping("/edit/{id}")
    public String modify(@PathVariable int id, @ModelAttribute GameItemRequestDTO gameItemRequestDTO) {
        log.info("--- 게임 아이템 수정 시작 (ItemID: {}) ---", id);
        log.debug("--- 수정 요청 데이터: {} ---", gameItemRequestDTO);

        gameItemService.modify(id, gameItemRequestDTO);

        log.info("게임 아이템 수정 성공 - id: {}", id);
        return "redirect:/admin/product/game";
    }

    /**
     * 게임 아이템 삭제 처리
     * POST /admin/product/game-items/delete/{id}
     */
    /* 삭제 아이템(재고) 삭제 처리 */
    // 삭제 가능 상태: DAMAGED, LOST
    // 삭제 불가 상태: NORMAL, RENTED → 백엔드에서 IllegalStateException 발생
    @PostMapping("/delete/{id}")
    public String remove(@PathVariable int id,
                         RedirectAttributes redirectAttributes) {
        log.info("--- 게임 아이템 삭제 요청 (ItemID: {}) ---", id);
//        gameItemService.remove(id);
        try {
            gameItemService.remove(id);
            log.info("--- 게임 아이템 삭제 완료 (ItemID: {}) ---", id);
            redirectAttributes.addFlashAttribute("successMessage", "아이템이 삭제되었습니다.");

        } catch (IllegalStateException e) {
            // NORMAL / RENTED 상태 → 삭제 불가
            log.warn("--- 게임 아이템 삭제 거부 (ItemID: {}) - 사유: {} ---", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        log.debug("게임 아이템 삭제 완료 - id: {}", id);
        return "redirect:/admin/product/game";
    }

    /* 게임 아이템 상태 변경 (단독 변경) */
    // ex) 대여가능 -> 수리중/분실 변경 시 사용
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable int id,
                               @RequestParam GameItemStatus status) {
        log.info("--- 아이템 상태 변경 요청 (id: {}, status: {}) ---", id, status);

        gameItemService.changeStatus(id, status);

        log.info("--- 상태 변경 완료 (id: {}, status: {}) ---", id, status);
        return "redirect:/admin/product/game";
    }
}
