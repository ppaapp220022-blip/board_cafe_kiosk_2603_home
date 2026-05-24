package org.example.board_cafe_kiosk_2603.dto.kiosk.cafePackage;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

/*
 * 작성자 : 김민기
 * 기능 : 도메인 VO 필드 정의
 * 날짜 : 2026-03-27
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafePackageDTO {

    private int           id;
    private String        name;
    private String        type;
    private Integer       durationMinutes;
    private int           basePrice;
    private Double        extraPricePerMin;
    private boolean       active;
    private LocalDateTime updatedAt;

    // === 요청(Request)용 필드 ===
    @Positive(message = "packageId는 1 이상이어야 합니다.")
    private int packageId;

    // === 응답(Response)용 필드 ===
    private boolean success;
    private String  message;
    private Integer tableNumber;
    /*
     * 작성자 : 김민기
     * 기능 : 정적 팩토리 메서드
     * 날짜 : 2026-03-27
     */

    public static CafePackageDTO fail(String message) {
        return CafePackageDTO.builder()
                .success(false)
                .message(message)
                .build();
    }

    /*
     * 작성자 : 김민기
     * 기능 : selected 메서드
     * 날짜 : 2026-03-27
     */

    public static CafePackageDTO selected(CafePackageDTO pkg, Integer tableNumber) {
        return CafePackageDTO.builder()
                .success(true)
                .name(pkg.getName())
                .basePrice(pkg.getBasePrice())
                .tableNumber(tableNumber)
                .build();
    }

    /*
     * 작성자 : 서민성
     * 기능 : getDisplayTime 메서드
     * 날짜 : 2026-03-31
     */

    public String getDisplayTime() { // package_selection.html에서 pkg.displayTime을 사용하고 있어서 추가함
        if (durationMinutes == null) return "Free";
        if (durationMinutes < 60) return durationMinutes + "분";
        return (durationMinutes / 60) + "시간";
    }
}
