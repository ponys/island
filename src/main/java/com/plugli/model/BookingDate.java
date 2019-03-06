package com.plugli.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "bookingdate")
public class BookingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookingdate_generator")
    @SequenceGenerator(name="bookingdate_generator", sequenceName = "bookingdate_seq")
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @NotNull
    @Column(unique = true)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="booking_id")
    private Booking booking;

    public BookingDate() {
    }

    public BookingDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
