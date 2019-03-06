package com.plugli.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    @RequestMapping(method = GET)
    public List<LocalDate> getAvailability(@RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        //TODO
        throw new ResponseStatusException(NOT_IMPLEMENTED);
    }

}
