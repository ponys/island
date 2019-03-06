package com.plugli.service;

import com.plugli.repository.BookingDateRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Resource
    BookingDateRepository bookingDateRepository;

    @Override
    public List<LocalDate> getAvailability(LocalDate startDate, LocalDate endDate) {
        return LongStream.range(startDate.toEpochDay(), endDate.toEpochDay())
                .mapToObj(LocalDate::ofEpochDay)
                .filter(day -> !bookingDateRepository.existsByDate(day))
                .collect(Collectors.toList());
    }
}
