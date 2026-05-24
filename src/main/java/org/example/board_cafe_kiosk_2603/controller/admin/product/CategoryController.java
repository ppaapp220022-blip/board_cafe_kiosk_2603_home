package org.example.board_cafe_kiosk_2603.controller.admin.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.admin.product.CategoryResponseDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;
import org.example.board_cafe_kiosk_2603.service.admin.product.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

/*
 * 작성자 : 서주연
 * 기능 : 카테고리 CRUD 컨트롤러
 * 날짜 : 2026-03-27
 */

@Log4j2
@Controller
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping
    public String getAll(Model model, PageRequestDTO pageRequestDTO) {
        log.info("--- 카테고리 목록 조회 요청 ---");

//        List<CategoryResponseDTO> list = categoryService.getAll();
//        model.addAttribute("categoryList", list);
//        model.addAttribute("activePage", "category");
        PageResponseDTO<CategoryResponseDTO> pageResponse = categoryService.getAll(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("activePage", "category");

        log.debug("카테고리 목록 조회 완료 - 건수: {}, 전체: {}", pageResponse.getDtoList().size(), pageResponse.getTotal());
        return "admin/category_list";
    }
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER')")
    public String register(@ModelAttribute CategoryRequestDTO categoryRequestDTO,
                           RedirectAttributes redirectAttributes) {
        log.info("--- 카테고리 등록 요청 (name: {}, type: {})",
                categoryRequestDTO.getName(), categoryRequestDTO.getType());

        try {
            categoryService.register(categoryRequestDTO);
            log.info("--- 카테고리 등록 성공: name={}", categoryRequestDTO.getName());
            redirectAttributes.addFlashAttribute("successMsg", "카테고리 등록되었습니다.");
        } catch (Exception e) {
            log.warn("--- 카테고리 등록 실패: {} ---", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMsg", "카테고리 등록에 실패했습니다: " + e.getMessage());
        }
        return "redirect:/admin/category";
    }
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER')")
    public String modify(@PathVariable int id,
                         @ModelAttribute CategoryRequestDTO categoryRequestDTO,
                         RedirectAttributes redirectAttributes) {
        log.info("--- 카테고리 수정 요청 (id={}, name={}, type={}) ---",
                id, categoryRequestDTO.getName(), categoryRequestDTO.getType());

//        categoryService.modify(id, categoryRequestDTO);
        try {
            categoryService.modify(id, categoryRequestDTO);
            log.info("--- 카테고리 수정 성공: {} ---", id);
            redirectAttributes.addFlashAttribute("successMsg", "카테고리가 수정되었습니다.");
        } catch (NoSuchElementException e) {
            log.warn("--- 수정 대상 카테고리 없음: (id:{}, Msg:{}) ---", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMsg", "수정할 카테고리를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.warn("--- 카테고리 수정 실패: (id:{}, Msg:{}) ---", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMsg", "카테고리 수정에 실패했습니다: " + e.getMessage());
        }
        log.debug("--- 카테고리 수정 완료, id: {} ---", id);
        return "redirect:/admin/category";
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER')")
    public String remove(@PathVariable int id,
                         RedirectAttributes redirectAttributes) {
        log.info("--- 카테고리 삭제 요청: {} ---", id);

        try {
            categoryService.remove(id);
            log.info("--- 카테고리 삭제 성공: {} ---", id);
            redirectAttributes.addFlashAttribute("successMsg", "카테고리가 삭제되었습니다.");
        } catch (IllegalStateException e) {
            // 연결 상품 존재 → 삭제 불가
            log.warn("--- 삭제 불가, (id: {}, Msg: {}) ---", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (NoSuchElementException e) {
            log.warn("--- 삭제 대상 없음 (id: {}, Msg: {}) ---", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMsg", "삭제할 카테고리를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.warn("--- 카테고리 삭제 실패 (id: {}, Msg: {}) ---", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMsg", "카테고리 삭제에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/admin/category";
    }
}
