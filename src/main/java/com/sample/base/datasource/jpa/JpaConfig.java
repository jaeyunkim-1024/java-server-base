package com.sample.base.datasource.jpa;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.sample.base.*.repository")
public class JpaConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.sample.base.*.entity"); // 엔티티 클래스 위치 지정

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Hibernate JPA 속성 설정
        Properties properties = new Properties();

        boolean showSql = false;
        boolean formatSql = false;
        boolean useSqlComment = false;

        showSql = true;
        formatSql = true;
        useSqlComment = true;

        // JPA Hibernate properties 설정
        properties.put(AvailableSettings.HBM2DDL_AUTO, "none"); // ddl-auto: none
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect"); // MySQL Dialect
        properties.put(AvailableSettings.SHOW_SQL, showSql); // SQL 출력 여부
        properties.put(AvailableSettings.FORMAT_SQL, formatSql); // SQL 포맷 여부
        properties.put(AvailableSettings.USE_SQL_COMMENTS, useSqlComment); // SQL 주석 활성화 여부
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY,
                PhysicalNamingStrategyStandardImpl.class.getName()); // 네이밍 전략
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY,
                SpringImplicitNamingStrategy.class.getName()); // 네이밍 전략
        properties.put("hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS", 100L); // 느린 쿼리 로그
        properties.put(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, 1000); // 배치 페치 크기

        em.setJpaProperties(properties);
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
