package org.example.board_cafe_kiosk_2603.mapper.admin.point;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.point.Point;
import org.example.board_cafe_kiosk_2603.domain.admin.point.PointHistory;
import org.example.board_cafe_kiosk_2603.dto.common.pagination.PageRequestDTO;

import java.util.List;


@Mapper

/*
 * 작성자 : 서민성
 * 기능 : 포인트 및 포인트 이력 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface PointMapper {

    /*
     * 작성자 : 김민기
     * 기능 : 전체 목록 조회
     * 날짜 : 2026-03-27
     */
    List<Point> findAll();

    /*
     * 작성자 : 김민기
     * 기능 : 전화번호 기준 조회
     * 날짜 : 2026-03-27
     */
    Point findByPhone(String phone);

    /*
     * 작성자 : 김민기
     * 기능 : 데이터 등록
     * 날짜 : 2026-03-27
     */
    void insert(Point point);

    /*
     * 작성자 : 김민기
     * 기능 : 포인트 잔액 변경
     * 날짜 : 2026-04-13
     */
    void updateBalance(Point point);

    /*
     * 작성자 : 서민성
     * 기능 : 총 포인트 잔액 합계 조회
     * 날짜 : 2026-03-27
     */
    int sumTotalBalance();

    /*
     * 작성자 : 김민기
     * 기능 : 전체 건수 조회
     * 날짜 : 2026-03-27
     */
    int countAll();

    /*
     * 작성자 : 김민기
     * 기능 : 포인트 이력 조회
     * 날짜 : 2026-03-27
     */
    List<PointHistory> findHistoryByPointId(int pointId);

    /*
     * 작성자 : 김민기
     * 기능 : 포인트 이력 등록
     * 날짜 : 2026-03-27
     */
    void insertHistory(PointHistory history);

    /*
     * 작성자 : 김민기
     * 기능 : 주문별 포인트 사용 이력 건수 조회
     * 날짜 : 2026-04-13
     */
    int countUseHistoryByOrderId(@Param("orderId") Long orderId);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 목록 조회
     * 날짜 : 2026-04-09
     */
    List<Point> selectList(PageRequestDTO pageRequestDTO);

    /*
     * 작성자 : 서민성
     * 기능 : 조건별 건수 조회
     * 날짜 : 2026-04-09
     */
    int selectCount(PageRequestDTO pageRequestDTO);
}

