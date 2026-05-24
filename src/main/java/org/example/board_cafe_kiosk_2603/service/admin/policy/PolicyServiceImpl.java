package org.example.board_cafe_kiosk_2603.service.admin.policy;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.policy.Policy;
import org.example.board_cafe_kiosk_2603.dto.admin.policy.PolicyDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;
import org.example.board_cafe_kiosk_2603.mapper.admin.policy.PolicyMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * 작성자 : 서민성
 * 기능 : Policy 관련 비즈니스 로직을 처리하는 서비스 구현체
 * 날짜 : 2026-04-03
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyMapper policyMapper;

    /*
     * 작성자 : 서민성
     * 기능 : getById 메서드
     * 날짜 : 2026-04-03
     */

    @Override
    public PolicyDTO getById(int id) {
        Policy policy = policyMapper.findById(id);
        return policy != null ? toDTO(policy) : null;
    }

    /*
     * 작성자 : 서민성
     * 기능 : insert 메서드
     * 날짜 : 2026-04-03
     */

    @Override
    public void insert(PolicyDTO policyDTO) {
        policyMapper.insert(toEntity(policyDTO));
        log.info("패키지 등록 완료 - name: {}, type: {}, price: {}",
                policyDTO.getName(), policyDTO.getType(), policyDTO.getBasePrice());
    }

    /*
     * 작성자 : 서민성
     * 기능 : 수정 처리
     * 날짜 : 2026-04-03
     */

    @Override
    public void update(PolicyDTO policyDTO) {
        policyMapper.update(toEntity(policyDTO));
        log.info("패키지 수정 완료 - id: {}, name: {}", policyDTO.getId(), policyDTO.getName());
    }

    /*
     * 작성자 : 서민성
     * 기능 : updateStatus 메서드
     * 날짜 : 2026-04-03
     */

    @Override
    public void updateStatus(int id, boolean active) {
        policyMapper.updateStatus(id, active);
        log.info("패키지 상태 변경 - id: {}, active: {}", id, active);
    }
    /*
     * 작성자 : 서민성
     * 기능 : 정책 목록 페이징 조회
     * 날짜 : 2026-04-09
     */

    @Override
    public PageResponseDTO<PolicyDTO> selectPagedPolicies(PageRequestDTO pageRequestDTO) {
        List<PolicyDTO> dtoList = policyMapper.selectList(pageRequestDTO).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        int total = policyMapper.selectCount(pageRequestDTO);

        return PageResponseDTO.<PolicyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    /*
     * 작성자 : 서민성
     * 기능 : getCount 메서드
     * 날짜 : 2026-04-09
     */

    @Override
    public int getCount(PageRequestDTO pageRequestDTO) {
        return policyMapper.selectCount(pageRequestDTO);
    }
    /*
     * 작성자 : 서민성
     * 기능 : DTO 변환
     * 날짜 : 2026-04-03
     */


    private PolicyDTO toDTO(Policy policy) {
        return PolicyDTO.builder()
                .id(policy.getId())
                .name(policy.getName())
                .type(policy.getType())
                .durationMinutes(policy.getDurationMinutes())
                .basePrice(policy.getBasePrice())
                .extraPricePerMin(policy.getExtraPricePerMin())
                .active(policy.isActive())
                .updatedAt(policy.getUpdatedAt())
                .build();
    }

    /*
     * 작성자 : 서민성
     * 기능 : toEntity 메서드
     * 날짜 : 2026-04-03
     */

    private Policy toEntity(PolicyDTO dto) {
        return Policy.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .durationMinutes(dto.getDurationMinutes())
                .basePrice(dto.getBasePrice())
                .extraPricePerMin(dto.getExtraPricePerMin())
                .active(dto.isActive())
                .build();
    }
}
