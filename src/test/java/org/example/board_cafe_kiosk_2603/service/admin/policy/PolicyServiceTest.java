package org.example.board_cafe_kiosk_2603.service.admin.policy;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.dto.admin.policy.PolicyDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 작성자 : 서민성
 * 기능 : PolicyService 테스트
 * 날짜 : 2026-04-03
 */

@Log4j2
@SpringBootTest
@Transactional
class PolicyServiceTest {
    @Autowired
    private PolicyService policyService;

    private PolicyDTO createPolicy(String name) {
        PolicyDTO dto = PolicyDTO.builder()
                .name(name)
                .type("HOURLY")
                .durationMinutes(120)
                .basePrice(10000)
                .extraPricePerMin(100.0)
                .active(true)
                .build();
        policyService.insert(dto);

        return policyService.selectPagedPolicies(PageRequestDTO.builder().page(1).size(100).build())
                .getDtoList()
                .stream()
                .filter(policy -> name.equals(policy.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("생성한 정책을 찾지 못했습니다."));
    }
    /*
     * 작성자 : 서민성
     * 기능 : 전체 정책 조회 테스트
     * 날짜 : 2026-04-03
     */

    @Test
    void testGetAllPolicies() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .build();

        PageResponseDTO<PolicyDTO> responseDTO = policyService.selectPagedPolicies(pageRequestDTO);
        log.info("전체 패키지 수: {}", responseDTO.getTotal());
        responseDTO.getDtoList().forEach(p -> log.info("패키지: {} | {} | {}원 | 활성: {}",
                p.getName(), p.getDisplayTime(), p.getBasePrice(), p.isActive()));
        assertNotNull(responseDTO.getDtoList());
    }
    /*
     * 작성자 : 서민성
     * 기능 : 활성 정책 조회 테스트
     * 날짜 : 2026-04-03
     */

    @Test
    void testGetAllPoliciesActiveFilter() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .filter("active")
                .build();

        PageResponseDTO<PolicyDTO> responseDTO = policyService.selectPagedPolicies(pageRequestDTO);
        log.info("활성 패키지 수: {}", responseDTO.getTotal());
        assertTrue(responseDTO.getDtoList().stream().allMatch(PolicyDTO::isActive));
    }
    /*
     * 작성자 : 서민성
     * 기능 : 비활성 정책 조회 테스트
     * 날짜 : 2026-04-03
     */

    @Test
    void testGetAllPoliciesInactiveFilter() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .filter("inactive")
                .build();

        PageResponseDTO<PolicyDTO> responseDTO = policyService.selectPagedPolicies(pageRequestDTO);
        log.info("비활성 패키지 수: {}", responseDTO.getTotal());
        assertTrue(responseDTO.getDtoList().stream().noneMatch(PolicyDTO::isActive));
    }

    /*
     * 작성자 : 서민성
     * 기능 : testSelectCount 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testSelectCount() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .build();

        PageResponseDTO<PolicyDTO> responseDTO = policyService.selectPagedPolicies(pageRequestDTO);
        log.info("전체 개수: {}", responseDTO.getTotal());
        assertTrue(responseDTO.getTotal() >= 0);
    }
    /*
     * 작성자 : 서민성
     * 기능 : 정책 단건 조회 테스트
     * 날짜 : 2026-04-03
     */

    @Test
    void testGetById() {
        PolicyDTO created = createPolicy("단건조회용_" + System.currentTimeMillis());
        PolicyDTO dto = policyService.getById(created.getId());
        log.info("단건 조회: {}", dto);
        assertNotNull(dto);
    }
    /*
     * 작성자 : 서민성
     * 기능 : 정책 등록 테스트
     * 날짜 : 2026-04-03
     */

    @Test
    void testInsert() {
        PolicyDTO dto = PolicyDTO.builder()
                .name("서비스 테스트 패키지")
                .type("HOURLY")
                .durationMinutes(120)
                .basePrice(10000)
                .extraPricePerMin(100.0)
                .active(true)
                .build();

        policyService.insert(dto);
        log.info("등록 완료");

        // 전체 개수로 확인
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(100)
                .build();

        PageResponseDTO<PolicyDTO> responseDTO = policyService.selectPagedPolicies(pageRequestDTO);
        assertTrue(responseDTO.getDtoList().stream()
                .anyMatch(p -> p.getName().equals("서비스 테스트 패키지")));
    }
    /*
     * 작성자 : 서민성
     * 기능 : 정책 수정 테스트
     * 날짜 : 2026-04-03
     */

    @Test
    void testUpdate() {
        PolicyDTO created = createPolicy("수정전_" + System.currentTimeMillis());
        PolicyDTO dto = PolicyDTO.builder()
                .id(created.getId())
                .name("수정된 서비스 패키지")
                .type("HOURLY")
                .durationMinutes(90)
                .basePrice(7000)
                .extraPricePerMin(60.0)
                .build();

        policyService.update(dto);
        PolicyDTO updated = policyService.getById(created.getId());
        log.info("수정 결과: {}", updated);
        assertEquals("수정된 서비스 패키지", updated.getName());
    }
    /*
     * 작성자 : 서민성
     * 기능 : 정책 활성 상태 변경 테스트
     * 날짜 : 2026-04-03
     */

    @Test
    void testUpdateStatus() {
        PolicyDTO created = createPolicy("상태변경용_" + System.currentTimeMillis());
        policyService.updateStatus(created.getId(), false);
        PolicyDTO dto = policyService.getById(created.getId());
        log.info("비활성화 결과: active={}", dto.isActive());
        assertFalse(dto.isActive());

        policyService.updateStatus(created.getId(), true);
        dto = policyService.getById(created.getId());
        log.info("활성화 결과: active={}", dto.isActive());
        assertTrue(dto.isActive());
    }

}
