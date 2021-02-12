package com.myworktechs.statcounter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class StatisticsMapConfig {
    @Bean
    public ConcurrentMap<String, AtomicLong> statisticsMap() {
        return new ConcurrentHashMap<>();
    }
}
