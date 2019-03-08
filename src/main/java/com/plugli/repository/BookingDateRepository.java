package com.plugli.repository;

import com.plugli.model.BookingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingDateRepository extends JpaRepository<BookingDate, Long> {

    boolean existsByDate(LocalDate date);

    Optional<BookingDate> findByDate(LocalDate date);

    @Query("SELECT b from BookingDate b WHERE date >= :startdate AND date < :enddate")
    List<BookingDate> findAllBetween(@Param("startdate") LocalDate startDate, @Param("enddate") LocalDate endDate);

}
