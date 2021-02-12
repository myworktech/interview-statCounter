package com.myworktechs.statcounter.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.Connection;
import java.util.Map;

@Slf4j
public class LiquibaseApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        try (HikariDataSource ds = new HikariDataSource()) {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            fillDsWithProperties(ds, environment);

            try (Connection connection = ds.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                try (Liquibase liquibase = new Liquibase("db/changelog-master.xml", new ClassLoaderResourceAccessor(), database)) {
                    liquibase.update(new Contexts(), new LabelExpression());
                } catch (LiquibaseException e) {
                    log.error("", e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fillDsWithProperties(HikariDataSource ds, ConfigurableEnvironment environment) {
        ds.setJdbcUrl(environment.getProperty("ds.url"));
        ds.setDriverClassName(environment.getProperty("ds.driver"));
    }
}