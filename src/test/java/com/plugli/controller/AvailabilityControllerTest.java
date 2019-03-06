package com.plugli.controller;

import com.plugli.service.AvailabilityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AvailabilityController.class)
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AvailabilityService availabilityService;

    private static String AVAILABILITY_PATH = "/availability";

    @Test
    public void testGetAvailabilityNoDates() throws Exception {
        given(availabilityService.getAvailability(LocalDate.now(), LocalDate.now().plusDays(30))).willReturn(
                Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), LocalDate.now().plusDays(4)));

        mockMvc.perform(get(AVAILABILITY_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void testGetAvailabilityStartDate() throws Exception {
        given(availabilityService.getAvailability(LocalDate.now().plusDays(2), LocalDate.now().plusDays(2).plusDays(30))).willReturn(
                Arrays.asList(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4)));

        mockMvc.perform(get(AVAILABILITY_PATH).param("startDate", LocalDate.now().plusDays(2).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetAvailabilityDates() throws Exception {
        given(availabilityService.getAvailability(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4))).willReturn(
                Collections.singletonList(LocalDate.now().plusDays(2)));

        mockMvc.perform(get(AVAILABILITY_PATH).param("startDate", LocalDate.now().plusDays(1).toString())
                .param("endDate",LocalDate.now().plusDays(4).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

}
