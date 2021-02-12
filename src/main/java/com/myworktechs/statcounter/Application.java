package com.myworktechs.statcounter;

import com.myworktechs.statcounter.config.LiquibaseApplicationContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.myworktechs.statcounter",
        exclude = {DataSourceAutoConfiguration.class, LiquibaseAutoConfiguration.class})
@EnableConfigurationProperties

public class Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .properties("spring.jpa.open-in-view:false")
                .initializers(new LiquibaseApplicationContextInitializer())
                .run(args);
    }
}