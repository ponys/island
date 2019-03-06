package com.plugli.controller;

import com.plugli.model.dto.BookingDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/booking")
public class BookingController {


    @RequestMapping(value = "/{id}", method = GET)
    public BookingDTO getBooking(@PathVariable Long id) {
        //TODO
        throw new ResponseStatusException(NOT_IMPLEMENTED);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public BookingDTO createBooking(@RequestBody @Valid final BookingDTO booking) {
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
