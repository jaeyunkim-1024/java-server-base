package com.sample.base.datasource.mybatis;

import com.sample.base.client.user.entity.LoginHistory;
import com.sample.base.client.user.entity.UserInfo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@ComponentScan(basePackages = "com.sample.base")
@MapperScan(
        basePackages = "com.sample.base.mappers",
        sqlSessionFactoryRef = "sqlSessionFactory"
)
@Configuration
public class MybatisConfig {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml")
        );
        setTypeAliases(sessionFactory);
        return sessionFactory.getObject();
    }

    private void setTypeAliases(SqlSessionFactoryBean sessionFactory){
        sessionFactory.setTypeAliases(
                 UserInfo.class
                , LoginHistory.class);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
