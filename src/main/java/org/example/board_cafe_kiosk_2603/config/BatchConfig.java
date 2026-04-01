package org.example.board_cafe_kiosk_2603.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.mapper.admin.statistics.StatMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

@Log4j2
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StatMapper statMapper;

    /**
     * [Job]: 전체 통계 배치 작업
     */
    @Bean
    public Job dailyRevenueJob() {
        return new JobBuilder("dailyRevenueJob", jobRepository)
                .start(cleanupStep())   // 1단계: 기존 데이터(요약/항목) 삭제
                .next(generateStep())   // 2단계: 신규 데이터(요약/항목) 생성
                .build();
    }

    /**
     * [Step 1]: 기존 통계 데이터 삭제 단계
     */
    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet(cleanupTasklet(null), transactionManager)
                .build();
    }

    /**
     * [Step 2]: 신규 통계 데이터 생성 단계
     */
    @Bean
    public Step generateStep() {
        return new StepBuilder("generateStep", jobRepository)
                .tasklet(generateTasklet(null), transactionManager)
                .build();
    }

    /**
     * [Cleanup Tasklet]: 중복 방지를 위해 기존 데이터를 먼저 지우는 로직
     */
    @Bean
    @StepScope
    public Tasklet cleanupTasklet(@Value("#{jobParameters['targetDate']}") String targetDateStr) {
        return (contribution, chunkContext) -> {
            LocalDate targetDate = LocalDate.parse(targetDateStr);
            log.info(">>> [배치 1단계] {} 날짜의 기존 통계 데이터를 삭제합니다.", targetDate);

            // 매퍼에 작성한 삭제 메서드 호출
            statMapper.deleteDailySummary(targetDate);      // 1. 일별 요약 삭제
            statMapper.deleteItemSalesHistory(targetDate);  // 2. 상품별 이력 삭제

            return RepeatStatus.FINISHED;
        };
    }

    /**
     * [Generate Tasklet]: 실제 DB 데이터를 집계하여 통계 테이블에 삽입하는 로직
     */
    @Bean
    @StepScope
    public Tasklet generateTasklet(@Value("#{jobParameters['targetDate']}") String targetDateStr) {
        return (contribution, chunkContext) -> {
            LocalDate targetDate = LocalDate.parse(targetDateStr);
            log.info(">>> [배치 2단계] {} 날짜의 통계를 생성합니다.", targetDate);

            // 매퍼에 작성한 삽입 메서드 호출
            statMapper.insertDailySummaryFromSessions(targetDate); // 1. 일별 요약 생성
            statMapper.insertItemSalesHistory(targetDate);         // 2. 상품별 이력 생성

            return RepeatStatus.FINISHED;
        };
    }
}