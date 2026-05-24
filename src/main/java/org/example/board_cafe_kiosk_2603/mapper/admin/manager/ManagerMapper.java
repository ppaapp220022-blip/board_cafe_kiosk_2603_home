package org.example.board_cafe_kiosk_2603.mapper.admin.manager;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.manager.Manager;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;

import java.util.List;
import java.util.Optional;


@Mapper

/*
 * 작성자 : 서주연
 * 기능 : 관리자 계정 데이터 접근 인터페이스
 * 날짜 : 2026-04-01
 */
public interface ManagerMapper {

    /*
     * 작성자 : 서주연
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-04-01
     */
    List<Manager> findAll();

    /*
     * 작성자 : 서주연
     * 기능 : 로그인 ID 기준 조회
     * 날짜 : 2026-04-08
     */
    Optional<Manager> findByLoginId(String loginId);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-09
     */
    void insert(Manager manager);

    /*
     * 작성자 : 서주연
     * 기능 : 활성 상태 변경
     * 날짜 : 2026-04-01
     */
    void updateActive(@Param("id") int id, @Param("isActive") boolean isActive);

    /*
     * 작성자 : 서주연
     * 기능 : 프로필 정보 수정
     * 날짜 : 2026-04-01
     */
    void updateProfileInfo(@Param("loginId") String loginId,
                           @Param("name") String name,
                           @Param("password") String password);

    /*
     * 작성자 : 서주연
     * 기능 : 로그인 ID로 이메일 조회
     * 날짜 : 2026-04-08
     */
    Optional<String> findEmailByLoginId(String loginId);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 목록 조회
     * 날짜 : 2026-04-09
     */
    List<Manager> selectList(PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 건수 조회
     * 날짜 : 2026-04-09
     */
    int selectCount(PageRequestDTO pageRequestDTO);
}

