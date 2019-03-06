package com.plugli.controller;

import com.plugli.model.Booking;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/booking")
public class BookingController {


    @RequestMapping(value = "/{id}", method = GET)
    public Booking getBooking(@PathVariable Long id) {
        //TODO
        throw new ResponseStatusException(NOT_IMPLEMENTED);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public Booking createBooking(@RequestBody @Valid final Booking booking) {
        //TODO
        throw new ResponseStatusException(NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    public String updateBooking(@PathVariable Long id) {
        //TODO
        throw new ResponseStatusException(NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void cancelBooking(@PathVariable Long id) {
        //TODO
        throw new ResponseStatusException(NOT_IMPLEMENTED);
    }

}
