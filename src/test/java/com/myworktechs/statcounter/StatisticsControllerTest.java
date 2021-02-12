package com.myworktechs.statcounter;

import liquibase.pro.packaged.C;
import liquibase.pro.packaged.T;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;


@RunWith(JUnit4.class)
public class StatisticsControllerTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void t1() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);

        Thread mainThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Thread t = new Thread(() -> {
                    for (int j = 0; j < 1000; j++) {
                        restTemplate.put("http://localhost:8080/update?country=ru", null);
                    }
                    countDownLatch.countDown();
                });

                t.start();
            }
        });
        mainThread.start();
        countDownLatch.await();

        String response = restTemplate.getForObject("http://localhost:8080/report", String.class);
        System.out.println(response);
    }
}