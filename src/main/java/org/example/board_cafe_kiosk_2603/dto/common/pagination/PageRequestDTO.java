package org.example.board_cafe_kiosk_2603.dto.common.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/*
 * 작성자 : 서민성
 * 기능 : 현재 페이지 번호 (기본값: 1, 최솟값: 1)
 * 날짜 : 2026-04-09
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    // 한 페이지에 보여줄 데이터 개수 (기본값: 10, 범위: 1~100)
    @Builder.Default
    @Min(value = 1)
    @Max(value = 100)
    @Positive
    private int size = 10;

    // 검색어
    private String keyword;

    // 필터 (예: "all", "active", "inactive")
    private String filter;
    /*
     * 작성자 : 서민성
     * 기능 : MyBatis LIMIT 절에 사용할 시작 위치 계산
     * 날짜 : 2026-04-09
     */

    public int getSkip() {
        return (Math.max(1, page) - 1) * size;
    }
    /*
     * 작성자 : 서민성
     * 기능 : 페이징 링크에 붙을 쿼리 파라미터 문자열 생성
     * 날짜 : 2026-04-09
     */

    public String getLink() {
        StringBuilder builder = new StringBuilder();
        builder.append("size=").append(this.size);

        if (this.keyword != null) {
            builder.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }

        if (this.filter != null && !this.filter.equals("all")) {
            builder.append("&filter=").append(this.filter);
        }

        return builder.toString();
    }
}
