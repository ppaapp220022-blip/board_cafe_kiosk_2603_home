package org.example.board_cafe_kiosk_2603.mapper.kiosk.cafePackage;

import org.apache.ibatis.annotations.Mapper;
import org.example.board_cafe_kiosk_2603.domain.kiosk.cafePackage.CafePackage;

import java.util.List;


@Mapper

/*
 * 작성자 : 김민기
 * 기능 : 카페 패키지 데이터 접근 인터페이스
 * 날짜 : 2026-03-27
 */
public interface CafePackageMapper {

    /*
     * 작성자 : 김민기
     * 기능 : 활성 데이터 목록 조회
     * 날짜 : 2026-03-27
     */
    List<CafePackage> findAllActive();

    /*
     * 작성자 : 김민기
     * 기능 : ID로 단건 조회
     * 날짜 : 2026-03-27
     */
    CafePackage findById(int id);
}
