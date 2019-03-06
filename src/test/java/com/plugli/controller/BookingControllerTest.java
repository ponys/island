package com.plugli.controller;

import com.plugli.TestUtils;
import com.plugli.model.Booking;
import com.plugli.model.dto.BookingDTO;
import com.plugli.service.BookingService;
import com.plugli.service.exception.InvalidBookingLengthException;
import com.plugli.service.exception.InvalidBookingRangeException;
import com.plugli.service.exception.UnavailableDatesException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static com.plugli.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest({BookingController.class, BookingConverter.class})
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BookingService bookingService;

    @Autowired
    private BookingConverter bookingConverter;

    private static String BOOKING_PATH = "/booking";

    // GET //

    @Test
    public void testGetBookingByID() throws Exception {
        BookingDTO booking = TestUtils.createBookingDTO(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2));
        booking.setId(1L);
        given(bookingService.getBookingById(1L)).willReturn(Optional.of(bookingConverter.toBooking(booking)));

        mockMvc.perform(get(BOOKING_PATH + "/" + 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(booking.getName()))
                .andExpect(jsonPath("$.email").value(booking.getEmail()))
                .andExpect(jsonPath("$.arrivalDate").value(booking.getArrivalDate().toString()))
                .andExpect(jsonPath("$.departureDate").value(booking.getDepartureDate().toString()));

    }

    @Test
    public void testGetNonExistentBookingByID() throws Exception {
        given(bookingService.getBookingById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get(BOOKING_PATH + "/" + 1L))
                .andExpect(status().isNotFound());
    }

    // POST //
    @Test
    public void testCreateBooking() throws Exception {
        BookingDTO booking = TestUtils.createBookingDTO(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2));

        given(bookingService.create(any(Booking.class))).willReturn(bookingConverter.toBooking(booking));

        mockMvc.perform(post(BOOKING_PATH)
                .content(asJsonString(booking)).contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(booking.getName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    public void testCreateUnavailableBooking() throws Exception {
        BookingDTO booking = TestUtils.createBookingDTO(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2));

        given(bookingService.create(any(Booking.class))).willThrow(UnavailableDatesException.class);

        mockMvc.perform(post(BOOKING_PATH)
                .content(asJsonString(booking)).contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateBookingInvalidLengthDates() throws Exception {
        BookingDTO booking = TestUtils.createBookingDTO(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2));

        given(bookingService.create(any(Booking.class))).willThrow(InvalidBookingLengthException.class);

        mockMvc.perform(post(BOOKING_PATH)
                .content(asJsonString(booking)).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateBookingInvalidRangeDates() throws Exception {
        BookingDTO booking = TestUtils.createBookingDTO(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2));

        given(bookingService.create(any(Booking.class))).willThrow(InvalidBookingRangeException.class);

        mockMvc.perform(post(BOOKING_PATH)
                .content(asJsonString(booking)).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
