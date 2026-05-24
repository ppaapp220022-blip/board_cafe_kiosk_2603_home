package org.example.board_cafe_kiosk_2603.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
/*
 * 작성자 : 서주연
 * 기능 : MariaDB 설정
 * 날짜 : 2026-04-29
 */



@Configuration
public class MariaDBConfig {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.mariadb")
    public DataSourceProperties mariaDataSourceProperties() {
        /* 데이터 소스 프로퍼티 빈 생성
           URL, 계정, 비밀번호 등을 저장 */
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = {"dataSource", "primaryDataSource"})
    public DataSource primaryDataSource() {
        /* 데이터베이스 연결 객체 생성 */
        return mariaDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Primary
    @Bean(name = "mariaJdbcTemplate")
    public JdbcTemplate mariaJdbcTemplate(DataSource mariaDataSource) {
        /* SQL 실행을 쉽게 해주는 Spring JDBC 객체 생성 */
        return new JdbcTemplate(mariaDataSource);
    }

    @Primary
    @Bean(name = {"transactionManager", "mariaTxManager"})
    public PlatformTransactionManager mariaTxManager(DataSource mariaDataSource) {
        /* 트랜잭션 관리자 생성 */
        return new DataSourceTransactionManager(mariaDataSource);
    }
}
