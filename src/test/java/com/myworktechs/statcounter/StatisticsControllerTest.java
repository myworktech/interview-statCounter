package com.myworktechs.statcounter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class StatisticsControllerTest {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void timedTest() throws InterruptedException, IOException {
        reset();

        CountDownLatch countDownLatch = new CountDownLatch(20);

        Thread mainThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Thread ruThread = new Thread(new CountryAwareTestRunnable(countDownLatch, "ru"));
                Thread cyThread = new Thread(new CountryAwareTestRunnable(countDownLatch, "cy"));

                ruThread.start();
                cyThread.start();
            }
        });
        long startTime = System.currentTimeMillis();
        mainThread.start();
        countDownLatch.await();
        long stopTime = System.currentTimeMillis();

        Thread.sleep(100L);
        String responseString = restTemplate.getForObject("http://localhost:8080/report", String.class);
        Map<String, Long> response = objectMapper.readValue(responseString, new TypeReference<Map<String, Long>>() {
        });
        System.out.println("Report: " + responseString);
        System.out.println("Time consumed, ms: " + (stopTime - startTime) );

        assertEquals((long) response.get("ru"), 50_000L);
        assertEquals((long) response.get("cy"), 50_000L);
    }

    private void reset() {
        restTemplate.getForObject("http://localhost:8080/reset", Object.class);
    }

    private static class CountryAwareTestRunnable implements Runnable {
        private final CountDownLatch countDownLatch;
        private final String country;

        public CountryAwareTestRunnable(CountDownLatch countDownLatch, String country) {
            this.countDownLatch = countDownLatch;
            this.country = country;
        }

        @Override
        public void run() {
            for (int j = 0; j < 5000; j++) {
                restTemplate.put("http://localhost:8080/update?country=" + country, null);
            }
            countDownLatch.countDown();
        }
    }
}