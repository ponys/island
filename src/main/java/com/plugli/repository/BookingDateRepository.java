package com.plugli.repository;

import com.plugli.model.BookingDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BookingDateRepository extends JpaRepository<BookingDate, Long> {

    boolean existsByDate(LocalDate date);

    Optional<BookingDate> findByDate(LocalDate date);
}
