package org.example.board_cafe_kiosk_2603.mapper.admin.policy;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.policy.Policy;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;

import java.util.List;


@Mapper

/*
 * 작성자 : 서민성
 * 기능 : 요금 정책 데이터 접근 인터페이스
 * 날짜 : 2026-04-03
 */
public interface PolicyMapper {

    /*
     * 작성자 : 서민성
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-04-03
     */
    Policy findById(int id);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 등록
     * 날짜 : 2026-04-03
     */
    void insert(Policy policy);

    /*
     * 작성자 : 서민성
     * 기능 : 데이터 수정
     * 날짜 : 2026-04-03
     */
    void update(Policy policy);

    /*
     * 작성자 : 서민성
     * 기능 : 상태 변경
     * 날짜 : 2026-04-03
     */
    void updateStatus(@Param("id") int id, @Param("active") boolean active);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 목록 조회
     * 날짜 : 2026-04-09
     */
    List<Policy> selectList(PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 건수 조회
     * 날짜 : 2026-04-09
     */
    int selectCount(PageRequestDTO pageRequestDTO);
}
