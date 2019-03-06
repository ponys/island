package com.plugli.service;

import com.plugli.model.Booking;
import com.plugli.model.BookingDate;
import com.plugli.repository.BookingRepository;
import com.plugli.service.exception.BookingNotFoundException;
import com.plugli.service.exception.InvalidBookingLengthException;
import com.plugli.service.exception.InvalidBookingRangeException;
import com.plugli.service.exception.UnavailableDatesException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LongSummaryStatistics;
import java.util.Optional;

import static com.plugli.CampsiteConfiguration.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Resource
    AvailabilityService availabilityService;

    @Resource
    BookingRepository bookingRepository;

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }


    @Override
    public Booking create(Booking booking) throws UnavailableDatesException {
        validateBooking(booking);
        if (availabilityService.isAvailable(booking.getBookingDates())) {
            try {
                return bookingRepository.save(booking);
            } catch (DataAccessException e) {
                throw new UnavailableDatesException();
            }
        } else {
            throw new UnavailableDatesException();
        }
    }

    @Override
    public Booking updateBooking(Booking booking) throws BookingNotFoundException, UnavailableDatesException {
        validateBooking(booking);
        validateUpdate(booking);
        if (availabilityService.isAvailableExcludingBooking(booking)) {
            try {
                return bookingRepository.save(booking);
            } catch (DataAccessException e) {
                throw new UnavailableDatesException();
            }
        } else {
            throw new UnavailableDatesException();
        }
    }

    @Override
    public void deleteBooking(Long id) throws BookingNotFoundException {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new BookingNotFoundException(id);
        }
    }

    private void validateUpdate(Booking booking) throws BookingNotFoundException {
        if (!bookingRepository.existsById(booking.getId())) {
            throw new BookingNotFoundException(booking.getId());
        }
    }

    private void validateBooking(Booking booking) {
        LongSummaryStatistics summaryStatistics = booking.getBookingDates().stream().map(BookingDate::getDate).mapToLong(LocalDate::toEpochDay)
                .summaryStatistics();
        long bookingDays = summaryStatistics.getCount();
        if (!(bookingDays >= MIN_RESERVATION_DAYS && bookingDays <= MAX_RESERVATION_DAYS)) {
            throw new InvalidBookingLengthException();
        }
        LocalDate arrivalDate = LocalDate.ofEpochDay(summaryStatistics.getMin());
        LocalDate departureDate = LocalDate.ofEpochDay(summaryStatistics.getMax());
        LocalDate now = LocalDate.now();
        if (now.isEqual(arrivalDate) || now.until(departureDate, ChronoUnit.DAYS) >= MAX_RESERVATION_IN_ADVANCE_DAYS) {
            throw new InvalidBookingRangeException();
        }
        if (arrivalDate.until(departureDate.plusDays(1)).getDays() != bookingDays) {
            throw new InvalidBookingRangeException();
        }
    }
}
