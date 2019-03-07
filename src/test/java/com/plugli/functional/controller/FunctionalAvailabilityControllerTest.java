package com.plugli.functional.controller;

import com.plugli.IslandApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IslandApplication.class)
@AutoConfigureMockMvc
public class FunctionalAvailabilityControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testConcurrentAvailability() throws Exception {
        int threads = 1000;
        List<Callable<Integer>> callableList = new ArrayList<>();
        for (int i = 1; i <= threads; i++) {
            final int j = ((29 + i) % 29) + 1;
            callableList.add(() ->
                    mockMvc.perform(get("/availability")
                        .param("startDate", LocalDate.now().plusDays(j).toString())
                        .param("departureDate", LocalDate.now().plusDays(j + 1).toString()))
                        .andReturn()
                        .getResponse().getStatus());
        }
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Future<Integer>> futures = executorService.invokeAll(callableList);

        for (Future<Integer> future : futures) {
            assertThat(future.get(), is(200));
        }
    }
}
