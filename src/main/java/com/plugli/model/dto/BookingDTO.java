package com.plugli.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(value = "Booking", description = "A Booking representing a reservation on the campsite")
public class BookingDTO {

    @ApiModelProperty(notes = "Unique booking ID representing the reservation", readOnly = true)
    private long id;

    @ApiModelProperty(notes = "Full name of the user that generates the reservation", required = true)
    @NotNull
    private  String name;

    @ApiModelProperty(notes = "Email of the the user that generates the reservation", required = true)
    @NotNull
    private  String email;

    @ApiModelProperty(notes = "Intended arrival date of the reservation", required = true)
    @NotNull
    private LocalDate arrivalDate;

    @ApiModelProperty(notes = "Intended departure date of the reservation", required = true)
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
