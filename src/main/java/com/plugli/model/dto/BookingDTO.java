package com.plugli.model.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingDTO {

    private long id;

    @NotNull
    private  String name;

    @NotNull
    private  String email;

    @NotNull
    private LocalDate arrivalDate;

    @NotNull
    private  LocalDate departureDate;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
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

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
}
