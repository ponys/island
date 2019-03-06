package com.plugli.service;

import com.plugli.model.BookingDate;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {

    List<LocalDate> getAvailability(LocalDate startDate, LocalDate endDate);

    boolean isAvailable(List<BookingDate> dates);
}
