package com.myworktechs.statcounter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatisticsAsyncPersister {

    private final int executeDelay;
    private final ConcurrentMap<String, AtomicLong> statisticsMap;
    private final StatisticsDao statisticsDao;

    public StatisticsAsyncPersister(@Value("${itemPersister.executeDelayMs}") int executeDelay,
                                    ConcurrentMap<String, AtomicLong> statisticsMap,
                                    StatisticsDao statisticsDao) {
        this.executeDelay = executeDelay;
        this.statisticsMap = statisticsMap;
        this.statisticsDao = statisticsDao;
    }

    @PostConstruct
    public void init() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
                new CustomizableThreadFactory("ItemAsyncPersistenceProcessor"));

        executor.scheduleWithFixedDelay(() -> statisticsDao.saveItem(
                new HashMap<>(statisticsMap)),
                10,
                executeDelay,
                TimeUnit.MILLISECONDS);
    }
}
