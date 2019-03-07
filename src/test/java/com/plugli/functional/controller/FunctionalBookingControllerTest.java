package com.plugli.functional.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugli.IslandApplication;
import com.plugli.TestUtils;
import com.plugli.model.dto.BookingDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.plugli.TestUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IslandApplication.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class FunctionalBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateConcurrentBookingsAllSameDays() throws Exception {
        int threads = 1000;
        LocalDate arrivalDate = LocalDate.now().plusDays(1);
        LocalDate departureDate = LocalDate.now().plusDays(3);

        BookingDTO booking = TestUtils.createBookingDTO(arrivalDate, departureDate);
        Callable<Integer> createBookingCallable = ()  -> mockMvc.perform(post("/booking")
                .content(asJsonString(booking)).contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse().getStatus();

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Future<Integer>> futures = executorService.invokeAll(IntStream.range(0, threads).mapToObj(i -> createBookingCallable).collect(Collectors.toList()));
        List<Integer> responses = new ArrayList<>();
        for (Future<Integer> future : futures) {
            responses.add(future.get());
        }
        assertThat(responses.stream().filter(i -> i == 201).count(), is(1L));
        assertThat(responses.stream().filter(i -> i == 409).count(), is((long) threads - 1));
    }

    @Test
    public void testCreateConcurrentBookingsMaxDays() throws Exception {
        int threads = 100;
        List<Callable<Integer>> callables = new ArrayList<>();
        for (int i = 1; i <= threads; i++) {
            final int j = ((29 + i) % 29) + 1;
            callables.add(() -> mockMvc.perform(post("/booking")
                    .content(asJsonString(TestUtils.createBookingDTO(LocalDate.now().plusDays(j),LocalDate.now().plusDays(j+1))))
                    .contentType(APPLICATION_JSON))
                    .andReturn()
                    .getResponse().getStatus());
        }
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Future<Integer>> futures = executorService.invokeAll(callables);
        List<Integer> responses = new ArrayList<>();
        for (Future<Integer> future : futures) {
            responses.add(future.get());
        }
        assertThat(responses.stream().filter(i -> i == 201).count(), is((long) 29));
        assertThat(responses.stream().filter(i -> i == 409).count(), is((long) threads - 29));
    }

    @Test
    public void testConcurrentCreateAndUpdates() throws Exception {
        List<BookingDTO> bookingForUpdates = new ArrayList<>();

        // Create first 10 Bookings
        for (int i = 1; i <= 10; i++) {
            bookingForUpdates.add(objectMapper.readValue(mockMvc.perform(post("/booking")
                    .content(asJsonString(TestUtils.createBookingDTO(LocalDate.now().plusDays(i),LocalDate.now().plusDays(i+1))))
                    .contentType(APPLICATION_JSON))
                    .andReturn().getResponse().getContentAsString(), BookingDTO.class));
        }
        // changes created booking for update to +10 days
        for (BookingDTO booking : bookingForUpdates) {
            booking.setArrivalDate(booking.getArrivalDate().plusDays(10));
            booking.setDepartureDate(booking.getDepartureDate().plusDays(10));
        }

        List<Callable<Integer>> callable = new ArrayList<>();
        // Create a set of thread for :
        // 10 threads to create in 10 days
        // 10 threads to update in same range
        for (int i = 1; i <= 10; i++) {
            final int j = i + 10;
            callable.add(() -> mockMvc.perform(post("/booking")
                    .content(asJsonString(TestUtils.createBookingDTO(
                            LocalDate.now().plusDays(j),
                            LocalDate.now().plusDays(j + 1))))
                    .contentType(APPLICATION_JSON))
                    .andReturn()
                    .getResponse().getStatus());
            BookingDTO bookingForUpdate = bookingForUpdates.get(i - 1);
            callable.add(() -> mockMvc.perform(put("/booking/" + bookingForUpdate.getId())
                    .content(asJsonString(bookingForUpdate))
                    .contentType(APPLICATION_JSON))
                    .andReturn()
                    .getResponse().getStatus());
        }
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // shuffle so either create/update can run first
        Collections.shuffle(callable);
        List<Future<Integer>> futures = executorService.invokeAll(callable);

        List<Integer> responses = new ArrayList<>();
        for (Future<Integer> future : futures) {
            responses.add(future.get());
        }

        // We expect only 10 successful calls either of them from create or update.
        assertThat(responses.stream().filter(i -> i == 200 || i == 201).count(), is(10L));
        assertThat(responses.stream().filter(i -> i == 409).count(), is(10L));
    }


}
