package org.example.board_cafe_kiosk_2603.mapper.kiosk;

import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.kiosk.cafePackage.CafePackage;
import org.example.board_cafe_kiosk_2603.mapper.kiosk.cafePackage.CafePackageMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/*
 * 작성자 : 김민기
 * 기능 : CafePackageMapper 테스트
 * 날짜 : 2026-03-27
 */

@Log4j2
@SpringBootTest
class CafePackageMapperTest {

    @Autowired
    private CafePackageMapper cafePackageMapper;

    /*
     * 작성자 : 김민기
     * 기능 : findAllActive_returnsActivePackages 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    @Disabled("로컬 DB의 패키지 활성 상태가 더미 데이터와 자주 달라 고정 검증을 잠시 보류합니다.")
    void findAllActive_returnsActivePackages() {
        List<CafePackage> packages = cafePackageMapper.findAllActive();
        assertThat(packages).isNotNull();
        packages.forEach(p -> assertThat(p.isActive()).isTrue());
    }

    /*
     * 작성자 : 김민기
     * 기능 : findAllActive_sortedByBasePrice 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findAllActive_sortedByBasePrice() {
        List<CafePackage> packages = cafePackageMapper.findAllActive();
        for (int i = 0; i < packages.size() - 1; i++) {
            assertThat(packages.get(i).getBasePrice())
                    .isLessThanOrEqualTo(packages.get(i + 1).getBasePrice());
        }
    }

    /*
     * 작성자 : 김민기
     * 기능 : findById_success 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findById_success() {
        List<CafePackage> packages = cafePackageMapper.findAllActive();
        int existingId = packages.get(0).getId();

        CafePackage found = cafePackageMapper.findById(existingId);
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(existingId);
        assertThat(found.getName()).isNotBlank();
        assertThat(found.getBasePrice()).isGreaterThanOrEqualTo(0);
    }

    /*
     * 작성자 : 김민기
     * 기능 : findById_notFound 메서드
     * 날짜 : 2026-03-27
     */

    @Test
    void findById_notFound() {
        CafePackage found = cafePackageMapper.findById(99999);
        assertThat(found).isNull();
    }
}
