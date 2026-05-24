package org.example.board_cafe_kiosk_2603.mapper.admin.point;

import org.apache.ibatis.annotations.Mapper;
import org.example.board_cafe_kiosk_2603.domain.admin.point.Customer;


@Mapper

/*
 * 작성자 : 서민성
 * 기능 : 고객 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface CustomerMapper {

    /*
     * 작성자 : 서민성
     * 기능 : 고객 정보 등록
     * 날짜 : 2026-03-27
     */
    void insertCustomer(Customer customer);

    /*
     * 작성자 : 서민성
     * 기능 : 전화번호로 고객 조회
     * 날짜 : 2026-03-27
     */
    Customer selectByPhone(String phone);
}
