package com.myworktechs.statcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatisticsDao {

    public static final String MERGE_ITEM = "MERGE INTO COUNTRY_STATISTICS KEY (COUNTRY) VALUES (:country, :counter)";
    public static final String DELETE_ALL = "DELETE FROM COUNTRY_STATISTICS";
    public static final String SELECT_VALUES = "SELECT COUNTRY, COUNTER FROM COUNTRY_STATISTICS";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StatisticsDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void clear() {
        namedParameterJdbcTemplate.update(DELETE_ALL, Collections.emptyMap());
    }

    public Map<String, AtomicLong> getInitialValues() {
        return namedParameterJdbcTemplate.query(SELECT_VALUES, rs -> {
            Map<String, AtomicLong> partnersMap = new HashMap<>();
            while (rs.next()) {
                String country = rs.getString("COUNTRY");
                long count = rs.getLong("COUNTER");
                partnersMap.put(country, new AtomicLong(count));
            }
            return partnersMap;
        });
    }

    public void saveItem(Map<String, AtomicLong> statisticsMap) {
        statisticsMap.forEach((key, value) -> saveItem(value.get(), key));
    }

    private void saveItem(long counterForCountry, String country) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country", country);
        parameterSource.addValue("counter", counterForCountry);

        namedParameterJdbcTemplate.update(MERGE_ITEM, parameterSource);
    }
}
