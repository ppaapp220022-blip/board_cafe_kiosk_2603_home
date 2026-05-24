package org.example.board_cafe_kiosk_2603.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.service.admin.statistics.StatService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value; // 👈 이 부분이 핵심입니다!
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
/*
 * 작성자 : 강수연
 * 기능 : [Spring Batch 설정] 매출 및 방문자 통계 생성을 위한 배치 설정 클래스
 * 날짜 : 2026-04-01
 */


@Log4j2
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StatService statService;
    @Bean
    public Job dailyRevenueJob() {
        log.info("--- Spring Batch dailyRevenueJob ---");
        return new JobBuilder("dailyRevenueJob", jobRepository)
                .start(statStep())
                .build();
    }
    @Bean
    public Step statStep() {
        log.info("--- Spring Batch statStep ---");
        return new StepBuilder("statStep", jobRepository)
                .tasklet(statTasklet(null), transactionManager)
                .build();
    }
    @Bean
    @StepScope
    public Tasklet statTasklet(@Value("#{jobParameters['targetDate']}") String targetDateStr) {
        return (contribution, chunkContext) -> {
            LocalDate targetDate = LocalDate.parse(targetDateStr);
            log.info("--- Spring Batch statTasklet ---");
            log.info("{} 날짜 통계 insert", targetDate);

            // 서비스의 공통 로직 호출
            statService.createDailyStatistics(targetDate);

            return RepeatStatus.FINISHED;
        };
    }
}
