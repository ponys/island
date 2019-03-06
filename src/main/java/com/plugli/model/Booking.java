package com.plugli.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_generator")
    @SequenceGenerator(name="booking_generator", sequenceName = "booking_seq")
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @NotNull
    private  String name;

    @NotNull
    private  String email;

    @NotNull
    @OneToMany(mappedBy = "booking", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BookingDate> bookingDates = new ArrayList<>();

    public Booking() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<BookingDate> getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(List<BookingDate> bookingDates) {
        this.bookingDates = bookingDates;
    }

}
