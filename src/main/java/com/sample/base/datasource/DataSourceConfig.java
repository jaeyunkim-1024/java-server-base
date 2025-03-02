package com.sample.base.datasource;

import com.sample.base.common.config.env.DotEnvScheme;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
    private final Dotenv dotenv;

    @Bean
    public DataSource dataSource() {
        String jdbcUrl = dotenv.get(DotEnvScheme.DB_URL.name());
        String username = dotenv.get(DotEnvScheme.DB_USERNAME.name());
        String password = dotenv.get(DotEnvScheme.DB_PASSWORD.name());

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver"); // DB 드라이버 변경 가능
        config.setJdbcUrl(jdbcUrl); // DB URL 설정
        config.setUsername(username);
        config.setPassword(password);

        // HikariCP 성능 튜닝 옵션 (필요에 따라 조정)
        config.setMaximumPoolSize(10);  // 최대 커넥션 풀 크기
        config.setMinimumIdle(2);       // 최소 유휴 커넥션 개수
        config.setIdleTimeout(30000);   // 유휴 커넥션 유지 시간 (30초)
        config.setConnectionTimeout(20000); // 커넥션 대기 시간 (20초)
        config.setMaxLifetime(1800000); // 커넥션 최대 유지 시간 (30분)

        return new HikariDataSource(config);
    }
}
