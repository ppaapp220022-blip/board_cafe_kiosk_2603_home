package org.example.board_cafe_kiosk_2603.service.admin.manager;

import org.example.board_cafe_kiosk_2603.dto.admin.manager.ManagerRequest;
import org.example.board_cafe_kiosk_2603.dto.admin.manager.ManagerResponse;
import org.example.board_cafe_kiosk_2603.dto.admin.manager.ProfileUpdateRequest;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageResponseDTO;

import java.util.List;
import java.util.Optional;

/*
 * 작성자 : 서주연
 * 기능 : 관리자 계정 관리 서비스 인터페이스
 * 날짜 : 2026-04-01
 */
public interface ManagerService {

    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-04-01
     */
    List<ManagerResponse> findAll();

    /*
     * 작성자 : 서주연
     * 기능 : 관리자 계정 등록
     * 날짜 : 2026-04-01
     */
    void createManager(ManagerRequest request);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태 변경
     * 날짜 : 2026-04-01
     */
    void updateActive(int id, boolean isActive);

    /*
     * 작성자 : 서주연
     * 기능 : 로그인 ID 중복 여부 확인
     * 날짜 : 2026-04-10
     */
    boolean isLoginIdDuplicate(String loginId);

    /*
     * 작성자 : 서민성
     * 기능 : 로그인 ID 기준 조회
     * 날짜 : 2026-04-09
     */
    Optional<ManagerResponse> findByLoginId(String loginId);

    /*
     * 작성자 : 서주연
     * 기능 : 프로필 수정
     * 날짜 : 2026-04-08
     */
    void updateProfile(String loginId, ProfileUpdateRequest request);

    /*
     * 작성자 : 서주연
     * 기능 : 임시 비밀번호 재설정
     * 날짜 : 2026-04-08
     */
    String resetPassword(String loginId);

    /*
     * 작성자 : 서주연
     * 기능 : 지정 비밀번호로 재설정
     * 날짜 : 2026-04-10
     */
    void resetPasswordTo(String loginId, String rawPassword);

    /*
     * 작성자 : 서민성
     * 기능 : 관리자 목록 페이징 조회
     * 날짜 : 2026-04-09
     */
    PageResponseDTO<ManagerResponse> getPagedManagers(PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 건수 조회
     * 날짜 : 2026-04-09
     */
    int getCount(PageRequestDTO pageRequestDTO);
}

