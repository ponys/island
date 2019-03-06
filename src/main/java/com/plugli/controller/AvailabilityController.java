package com.plugli.controller;

import com.plugli.service.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    @Resource
    AvailabilityService availabilityService;

    @RequestMapping(method = GET)
    public List<LocalDate> getAvailability(@RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null)
            startDate = LocalDate.now();
        if (endDate == null)
            endDate = startDate.plusDays(30);

        return availabilityService.getAvailability(startDate, endDate);
    }

}
