package com.plugli.service;

import com.plugli.model.Booking;
import com.plugli.service.exception.BookingNotFoundException;
import com.plugli.service.exception.UnavailableDatesException;

import java.util.Optional;

public interface BookingService {

    Optional<Booking> getBookingById(Long id);

    Booking create(Booking booking) throws UnavailableDatesException;

    Booking updateBooking(Booking toBooking) throws BookingNotFoundException, UnavailableDatesException;

    void deleteBooking(Long id) throws BookingNotFoundException;
}

