package com.myworktechs.statcounter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(JUnit4.class)
public class InvalidCountryTest {

    private static final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void invalidCountryTest() {
        try {
            restTemplate.put("http://localhost:8080/update?country=qwe", null);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(400, e.getRawStatusCode());
        }
    }
}