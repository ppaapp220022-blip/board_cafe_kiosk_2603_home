package org.example.board_cafe_kiosk_2603.repository.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.board_cafe_kiosk_2603.domain.admin.CafeTable;

import java.util.List;

/**
 * 대시보드 테이블 현황 전용 DAO 인터페이스
 * select와 update 기능에 집중하여 설계
 */
@Mapper
public interface CafeTableRepository {
    /** * [Select] 대시보드 메인용 전체 테이블 20개 조회
     * DB의 모든 테이블 리스트를 번호 순으로 반환함
     */
    List<CafeTable> selectAllTables();

    /**
     * [핵심] 테이블 상태 및 체크인 시간 업데이트
     * @Param id: 테이블 PK
     * @Param status: 'EMPTY', 'OCCUPIED', 'CLEANING' 등
     * 주 설명: 상태값에 따라 DB 내부 CASE 문이 check_in_time을 자동 제어함
     */
    int updateTableStatus(@Param("id") Integer id, @Param("status") String status);

    /**
     * [핵심] 특정 테이블의 액세스 토큰만 개별 변경
     * @Param id: 테이블 PK
     * @Param accessToken: 새로 발급된 인증 토큰 (UUID 등)
     * 상세 설명: 테이블 상태나 시간에 영향을 주지 않고 인증 세션만 갱신 시 사용
     */
    int updateAccessToken(@Param("id") Integer id, @Param("accessToken") String accessToken);

    /**
     * [핵심] 자정 기준 전체 데이터 초기화
     * 주 설명: 매일 AM 00:00 스케줄러에 의해 호출되어 모든 테이블을 공석으로 만듦
     * 상세 설명: 모든 테이블의 status='EMPTY', 시간=NULL, 토큰=NULL 처리
     */
    int updateAllTablesForNewDay();
}
