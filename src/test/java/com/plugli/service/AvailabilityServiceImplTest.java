package com.plugli.service;

import com.plugli.repository.BookingDateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AvailabilityServiceImplTest {

    @Mock
    BookingDateRepository bookingDateRepository;

    @InjectMocks
    AvailabilityServiceImpl availabilityService;

    @Test
    public void testGetAvailability() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate endDate = start.plusDays(5);

        given(bookingDateRepository.existsByDate(start.plusDays(2))).willReturn(true);

        List<LocalDate> rList = availabilityService.getAvailability(start, endDate);
        assertThat(rList.size(), is(4));
        assertThat(rList.contains(start.plusDays(2)), is(false));
    }

    @Test
    public void testNoAvailability() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate endDate = start.plusDays(5);
        given(bookingDateRepository.existsByDate(any(LocalDate.class))).willReturn(true);

        List<LocalDate> rList = availabilityService.getAvailability(start, endDate);
        assertThat(rList, is(empty()));
    }

}
