package com.myworktechs.statcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final ConcurrentMap<String, AtomicLong> statisticsMap;
    private final StatisticsDao statisticsDao;

    @Autowired
    public StatisticsService(ConcurrentMap<String, AtomicLong> statisticsMap,
                             StatisticsDao statisticsDao) {
        this.statisticsMap = statisticsMap;
        this.statisticsDao = statisticsDao;
    }

    @PostConstruct
    private void init() {
        Map<String, AtomicLong> map = statisticsDao.getInitialValues();
        statisticsMap.putAll(map);
    }

    public void updateCountry(String country) {
        AtomicLong atomicLong = statisticsMap.get(country);
        if (atomicLong == null) {
            atomicLong = new AtomicLong(0L);
            AtomicLong previous = statisticsMap.putIfAbsent(country, atomicLong);
            atomicLong = previous != null ? previous : atomicLong;
        }
        atomicLong.incrementAndGet();
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
        statisticsDao.clear();

    }
}
