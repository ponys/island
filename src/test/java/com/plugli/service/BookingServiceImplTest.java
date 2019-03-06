package com.plugli.service;

import com.plugli.TestUtils;
import com.plugli.model.Booking;
import com.plugli.repository.BookingRepository;
import com.plugli.service.exception.BookingNotFoundException;
import com.plugli.service.exception.InvalidBookingLengthException;
import com.plugli.service.exception.InvalidBookingRangeException;
import com.plugli.service.exception.UnavailableDatesException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.plugli.TestUtils.tomorrow;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    AvailabilityService availabilityService;

    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    public void testGetBookingById() {
        Booking booking = TestUtils.createBooking(tomorrow());

        given(bookingRepository.findById(1L)).willReturn(Optional.of(booking));
        Optional<Booking> rBooking = bookingService.getBookingById(1L);

        assertThat(rBooking.isPresent(), is(true));
        assertThat(rBooking.get(),is(booking));
    }

    @Test
    public void testGetNonExistentBookingById() {
        given(bookingRepository.findById(1L)).willReturn(Optional.empty());
        Optional<Booking> rBooking = bookingService.getBookingById(1L);

        assertThat(rBooking.isPresent(), is(false));
    }

    @Test
    public void testCreateValidBooking() throws UnavailableDatesException {
        Booking booking = TestUtils.createBooking(tomorrow());

        given(availabilityService.isAvailable(booking.getBookingDates())).willReturn(true);
        given(bookingRepository.save(booking)).willReturn(booking);
        Booking rBooking = bookingService.create(booking);

        assertThat(rBooking, is(booking));
    }

    @Test(expected = InvalidBookingRangeException.class)
    public void testCreateBookingForToday() throws UnavailableDatesException {
        Booking booking = TestUtils.createBooking(Collections.singletonList(LocalDate.now()));
        bookingService.create(booking);

    }

    @Test(expected = InvalidBookingRangeException.class)
    public void testCreateBookingForNextMonth() throws UnavailableDatesException {
        Booking booking = TestUtils.createBooking(Collections.singletonList(LocalDate.now().plusDays(31)));
        bookingService.create(booking);
    }

    @Test(expected = InvalidBookingLengthException.class)
    public void testCreateBookingWrongInterval() throws UnavailableDatesException {
        Booking booking = TestUtils.createBooking(Collections.emptyList());
        bookingService.create(booking);
    }

    @Test(expected = UnavailableDatesException.class)
    public void testCreateBookingOnUnavailableDates() throws UnavailableDatesException {
        Booking booking = TestUtils.createBooking(tomorrow());
        given(availabilityService.isAvailable(booking.getBookingDates())).willReturn(false);
        bookingService.create(booking);
    }

    @Test
    public void testUpdateBooking() throws UnavailableDatesException, BookingNotFoundException {
        Booking booking = TestUtils.createBooking(tomorrow());
        booking.setId(1L);
        given(availabilityService.isAvailableExcludingBooking(booking)).willReturn(true);
        given(bookingRepository.existsById(1L)).willReturn(true);
        given(bookingRepository.save(booking)).willReturn(booking);
        Booking rBooking = bookingService.updateBooking(booking);

        assertThat(rBooking, is(booking));
    }

    @Test(expected = BookingNotFoundException.class)
    public void testUpdateNonExistingBooking() throws UnavailableDatesException, BookingNotFoundException {
        Booking booking = TestUtils.createBooking(tomorrow());
        booking.setId(1L);
        given(bookingRepository.existsById(1L)).willReturn(false);
        bookingService.updateBooking(booking);
    }

    @Test
    public void testDeleteExistingBooking() throws BookingNotFoundException {
        given(bookingRepository.existsById(1L)).willReturn(true);
        bookingService.deleteBooking(1L);
    }

    @Test(expected = BookingNotFoundException.class)
    public void testDeleteNonExistingBooking() throws BookingNotFoundException {
        given(bookingRepository.existsById(1L)).willReturn(false);
        bookingService.deleteBooking(1L);
    }
}
