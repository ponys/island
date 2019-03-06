package com.plugli.service;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {

    List<LocalDate> getAvailability(LocalDate startDate, LocalDate endDate);

}
