package com.plugli.controller;

import com.plugli.model.Booking;
import com.plugli.model.BookingDate;
import com.plugli.model.dto.BookingDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Component
public class BookingConverter {

    @Resource
    ModelMapper modelMapper;

    Booking toBooking(BookingDTO bookingDTO) {
        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        List<BookingDate> bookingDates = LongStream.range(bookingDTO.getArrivalDate().toEpochDay(), bookingDTO.getDepartureDate().toEpochDay())
                .mapToObj(LocalDate::ofEpochDay)
                .map(BookingDate::new).collect(Collectors.toList());
        bookingDates.forEach(booking::addBookingDate);
        return booking;
    }

    BookingDTO toBookingDTO(Booking booking) {
        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
        LocalDate arrivalDate = booking.getBookingDates().stream().map(BookingDate::getDate).min(Comparator.naturalOrder()).get();
        LocalDate departureDate = arrivalDate.plusDays(booking.getBookingDates().size());
        bookingDTO.setArrivalDate(arrivalDate);
        bookingDTO.setDepartureDate(departureDate);
        return bookingDTO;
    }
}
