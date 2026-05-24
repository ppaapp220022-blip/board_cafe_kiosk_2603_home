package org.example.board_cafe_kiosk_2603.mapper.admin;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.point.Customer;
import org.example.board_cafe_kiosk_2603.mapper.admin.point.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


/*
 * 작성자 : 서민성
 * 기능 : CustomerMapper 테스트
 * 날짜 : 2026-03-27
 */

@Log4j2
@SpringBootTest
@Transactional
class CustomerMapperTest {
    @Autowired
    private CustomerMapper customerMapper;

    private String uniquePhone() {
        return "010" + String.format("%08d", (System.currentTimeMillis() % 100_000_000));
    }

    /*
     * 작성자 : 서민성
     * 기능 : insertCustomerTest 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void insertCustomerTest() {
        String phone = uniquePhone();
        Customer customer = Customer.builder()
                .phone(phone)
                .isActive(true)
                .build();


        customerMapper.insertCustomer(customer);
        log.info("등록된 고객: {}", customer);

    }

    /*
     * 작성자 : 서민성
     * 기능 : selectByPhoneTest 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void selectByPhoneTest() {
        String phone = uniquePhone();
        customerMapper.insertCustomer(Customer.builder()
                .phone(phone)
                .isActive(true)
                .build());
        Customer found = customerMapper.selectByPhone(phone);
        log.info("조회된 고객: {}", found);
    }

}
