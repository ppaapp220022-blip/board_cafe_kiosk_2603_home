package org.example.board_cafe_kiosk_2603.mapper.admin.policy;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.policy.Policy;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 작성자 : 서민성
 * 기능 : PolicyMapper 테스트
 * 날짜 : 2026-04-03
 */

@Log4j2
@SpringBootTest
@Transactional
class PolicyMapperTest {

    @Autowired
    private PolicyMapper policyMapper;

    private Policy createPolicy(String name) {
        Policy policy = Policy.builder()
                .name(name)
                .type("HOURLY")
                .durationMinutes(60)
                .basePrice(5000)
                .extraPricePerMin(50.0)
                .active(true)
                .build();
        policyMapper.insert(policy);
        return policy;
    }

    /*
     * 작성자 : 서민성
     * 기능 : testSelectList 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testSelectList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .build();

        List<Policy> list = policyMapper.selectList(pageRequestDTO);
        log.info("전체 패키지 수: {}", list.size());
        list.forEach(p -> log.info("패키지: {}", p));
        assertNotNull(list);
    }

    /*
     * 작성자 : 서민성
     * 기능 : testSelectListActiveFilter 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testSelectListActiveFilter() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .filter("active")
                .build();

        List<Policy> list = policyMapper.selectList(pageRequestDTO);
        log.info("활성 패키지 수: {}", list.size());
        list.forEach(p -> log.info("활성 패키지: {}", p));
        assertTrue(list.stream().allMatch(Policy::isActive));
    }

    /*
     * 작성자 : 서민성
     * 기능 : testSelectListInactiveFilter 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testSelectListInactiveFilter() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .filter("inactive")
                .build();

        List<Policy> list = policyMapper.selectList(pageRequestDTO);
        log.info("비활성 패키지 수: {}", list.size());
        list.forEach(p -> log.info("비활성 패키지: {}", p));
        assertTrue(list.stream().noneMatch(Policy::isActive));
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

        int count = policyMapper.selectCount(pageRequestDTO);
        log.info("전체 개수: {}", count);
        assertTrue(count >= 0);
    }

    /*
     * 작성자 : 서민성
     * 기능 : testFindById 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testFindById() {
        Policy seeded = createPolicy("단건조회용_" + System.currentTimeMillis());
        Policy policy = policyMapper.findById(seeded.getId());
        log.info("단건 조회: {}", policy);
        assertNotNull(policy);
    }

    /*
     * 작성자 : 서민성
     * 기능 : testInsert 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testInsert() {
        Policy policy = Policy.builder()
                .name("테스트 패키지")
                .type("HOURLY")
                .durationMinutes(60)
                .basePrice(5000)
                .extraPricePerMin(50.0)
                .active(true)
                .build();

        policyMapper.insert(policy);
        log.info("등록 완료 - id: {}", policy.getId());
        assertTrue(policy.getId() > 0);
    }

    /*
     * 작성자 : 서민성
     * 기능 : testUpdate 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testUpdate() {
        Policy created = createPolicy("수정전_" + System.currentTimeMillis());
        Policy policy = Policy.builder()
                .id(created.getId())
                .name("수정된 패키지")
                .type("HOURLY")
                .durationMinutes(90)
                .basePrice(7000)
                .extraPricePerMin(60.0)
                .build();

        policyMapper.update(policy);
        Policy updated = policyMapper.findById(created.getId());
        log.info("수정 결과: {}", updated);
        assertEquals("수정된 패키지", updated.getName());
    }

    /*
     * 작성자 : 서민성
     * 기능 : testUpdateStatus 메서드
     * 날짜 : 2026-04-03
     */

    @Test
    void testUpdateStatus() {
        Policy created = createPolicy("상태변경용_" + System.currentTimeMillis());

        policyMapper.updateStatus(created.getId(), false);
        Policy policy = policyMapper.findById(created.getId());
        log.info("비활성화 결과: {}", policy);
        assertFalse(policy.isActive());

        policyMapper.updateStatus(created.getId(), true);
        policy = policyMapper.findById(created.getId());
        log.info("활성화 결과: {}", policy);
        assertTrue(policy.isActive());
    }
}
