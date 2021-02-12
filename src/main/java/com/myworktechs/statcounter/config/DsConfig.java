package com.myworktechs.statcounter.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DsConfig {

    @Bean
    public DataSource dataSource(Environment environment) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(environment.getProperty("ds.url"));
        ds.setDriverClassName(environment.getProperty("ds.driver"));
        return ds;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}