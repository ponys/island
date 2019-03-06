package com.plugli.service;

import com.plugli.model.Booking;
import com.plugli.service.exception.UnavailableDatesException;

import java.util.Optional;

public interface BookingService {

    Optional<Booking> getBookingById(Long id);

    Booking create(Booking booking) throws UnavailableDatesException, UnavailableDatesException;
}
