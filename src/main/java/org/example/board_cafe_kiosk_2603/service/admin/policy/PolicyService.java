package org.example.board_cafe_kiosk_2603.service.admin.policy;

import org.example.board_cafe_kiosk_2603.dto.admin.policy.PolicyDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;

/*
 * 작성자 : 서민성
 * 기능 : 요금 정책 서비스 인터페이스
 * 날짜 : 2026-04-03
 */
public interface PolicyService {
    /*
     * 작성자 : 서민성
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-04-03
     */
    PolicyDTO getById(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-09
     */
    void insert(PolicyDTO policyDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 수정
     * 날짜 : 2026-04-03
     */
    void update(PolicyDTO policyDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 상태 변경
     * 날짜 : 2026-04-03
     */
    void updateStatus(int id, boolean active);

    /*
     * 작성자 : 서민성
     * 기능 : selectPagedPolicies 처리
     * 날짜 : 2026-04-09
     */
    PageResponseDTO<PolicyDTO> selectPagedPolicies(PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 건수 조회
     * 날짜 : 2026-04-09
     */
    int getCount(PageRequestDTO pageRequestDTO);
}
