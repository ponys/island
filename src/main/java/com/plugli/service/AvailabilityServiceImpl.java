package com.plugli.service;

import com.plugli.model.Booking;
import com.plugli.model.BookingDate;
import com.plugli.repository.BookingDateRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Resource
    private BookingDateRepository bookingDateRepository;

    @Override
    public List<LocalDate> getAvailability(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> bookingDates = bookingDateRepository.findAllBetween(startDate, endDate)
                .stream().map(BookingDate::getDate).collect(Collectors.toSet());

        return LongStream.range(startDate.toEpochDay(), endDate.toEpochDay())
                .mapToObj(LocalDate::ofEpochDay)
                .filter(day -> !bookingDates.contains(day))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAvailable(List<BookingDate> dates) {
        return dates.stream().noneMatch(date -> bookingDateRepository.existsByDate(date.getDate()));
    }

    @Override
    public boolean isAvailableExcludingBooking(Booking booking) {
        return booking.getBookingDates().stream().noneMatch(date -> {
            Optional<BookingDate> bookingDateO = bookingDateRepository.findByDate(date.getDate());
            return bookingDateO.isPresent() && bookingDateO.get().getBooking().getId() != booking.getId();
        });
    }

}
