package org.example.board_cafe_kiosk_2603.service.admin.cafeTable;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.domain.admin.CafeTable;
import org.example.board_cafe_kiosk_2603.dto.admin.CafeTableDTO;
import org.example.board_cafe_kiosk_2603.repository.admin.CafeTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CafeTableServiceImpl implements CafeTableService {
    private final CafeTableRepository cafeTableRepository;

    @Override
    public List<CafeTableDTO> getAllTableStatus() {
        /* DB 엔티티를 DTO 리스트로 변환하여 반환 */
        List<CafeTableDTO> cafeTableDTOList = new ArrayList<>();

        List<CafeTable> cafeTableList = cafeTableRepository.selectAllTables();
        cafeTableList.forEach(cafeTable -> {
            CafeTableDTO cafeTableDTO = CafeTableDTO.builder()
                    .tableNumber(cafeTable.getTableNumber())
                    .status(cafeTable.getStatus())
                    .checkInTime(cafeTable.getCheckInTime())
                    .id(cafeTable.getId())
                    .accessToken(cafeTable.getAccessToken()).build();
            cafeTableDTOList.add(cafeTableDTO);
        });
        return cafeTableDTOList;
    }

    @Override
    public void changeTableStatus(Integer id, String status) {
        /**
         * 상태 변경 시 Repository의 CASE 문을 통해 시간 자동 제어
         * 'EMPTY'로 변경 시 입장 시간도 함께 NULL 처리됨
         */
        log.info("테이블 ID: {} 상태 변경 -> {}", id, status);
        cafeTableRepository.updateTableStatus(id, status);
    }

    @Override
    public String generateNewToken(Integer id) {
        /* UUID를 생성하여 특정 테이블의 access_token 단독 갱신 */
        String newToken = UUID.randomUUID().toString().substring(0, 16).toUpperCase(); // 8자리 짧은 토큰 예시

        cafeTableRepository.updateAccessToken(id, newToken);
        log.info("테이블 ID: {} 새 토큰 발급: {}", id, newToken);
        return newToken;
    }

    @Override
    public void resetAllTablesForNewDay() {
        /* 스케줄러에서 호출하는 전체 초기화 로직 */
        int count = cafeTableRepository.updateAllTablesForNewDay();
        log.info("자정 데이터 리셋 완료: {}개 테이블 초기화", count);
    }
}
