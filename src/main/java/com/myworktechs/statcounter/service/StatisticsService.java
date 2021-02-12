package com.myworktechs.statcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ConcurrentMap<String, AtomicLong> statisticsMap = new ConcurrentHashMap<>();

    @Autowired
    public StatisticsService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @PostConstruct
    private void init() {
        Map<String, AtomicLong> map = namedParameterJdbcTemplate.query("SELECT COUNTRY, COUNTER FROM COUNTRY_STATISTICS", rs -> {
            Map<String, AtomicLong> partnersMap = new HashMap<>();
            while (rs.next()) {
                String country = rs.getString("COUNTRY");
                long count = rs.getLong("COUNTER");
                partnersMap.put(country, new AtomicLong(count));
            }
            return partnersMap;
        });
        statisticsMap.putAll(map);
    }

    public void updateCountry(String country) {
        AtomicLong atomicLong = statisticsMap.get(country);
        if (atomicLong == null) {
            atomicLong = new AtomicLong(0L);
            AtomicLong previous = statisticsMap.putIfAbsent(country, atomicLong);
            atomicLong = previous != null ? previous : atomicLong;
        }
        long counterForCountry = atomicLong.incrementAndGet();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country", country);
        parameterSource.addValue("counter", counterForCountry);

        namedParameterJdbcTemplate.update("MERGE INTO COUNTRY_STATISTICS KEY (COUNTRY) VALUES (:country, :counter)", parameterSource);
    }

    public Map<String, Long> report() {
        return statisticsMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    /**
     * May produce side-effects.
     */
    public void reset() {
        statisticsMap.clear();
        namedParameterJdbcTemplate.update("DELETE FROM COUNTRY_STATISTICS", Collections.emptyMap());

    }
}
