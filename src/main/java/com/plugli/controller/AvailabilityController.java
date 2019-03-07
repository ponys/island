package com.plugli.controller;

import com.plugli.service.AvailabilityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Availability Controller", description = "Endpoint that provides information of the availability of the campsite")
public class AvailabilityController {

    @Resource
    AvailabilityService availabilityService;

    @ApiOperation(value = "Get availability of the campsite for a given date range", response = String.class, responseContainer = "List")
    @RequestMapping(method = GET)
    public List<LocalDate> getAvailability(@ApiParam( value = "Start date of the availability range in the ISO 8601 format yyyy-mm-dd, it will set to today if not set")
                                               @RequestParam(required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @ApiParam( value = "End date of the availability range in the ISO 8601 format yyy-mm-dd, it will set to 30 days after start day if not set ")
                                           @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null)
            startDate = LocalDate.now();
        if (endDate == null)
            endDate = startDate.plusDays(30);

        return availabilityService.getAvailability(startDate, endDate);
    }

}
