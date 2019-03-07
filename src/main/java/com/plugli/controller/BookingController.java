package com.plugli.controller;

import com.plugli.controller.error.ErrorResponse;
import com.plugli.model.Booking;
import com.plugli.model.dto.BookingDTO;
import com.plugli.service.BookingService;
import com.plugli.service.exception.BookingNotFoundException;
import com.plugli.service.exception.InvalidBookingLengthException;
import com.plugli.service.exception.InvalidBookingRangeException;
import com.plugli.service.exception.UnavailableDatesException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/booking")
@Api(description = "Endpoint for interacting with bookings on the campsite")
public class BookingController {

    @Resource
    private BookingService bookingService;

    @Resource
    private BookingConverter bookingConverter;

    @ApiOperation(value = "Get a booking by ID", response = BookingDTO.class)
    @ApiResponses(value = @ApiResponse(code = 404, message = "Booking not found"))
    @RequestMapping(value = "/{id}", method = GET)
    public BookingDTO getBooking(@PathVariable Long id) throws BookingNotFoundException {
        Optional<Booking> oBooking = bookingService.getBookingById(id);
        return bookingConverter.toBookingDTO(oBooking.orElseThrow(() -> new BookingNotFoundException(id)));
    }

    @ApiOperation(value = "Create a Booking", response = BookingDTO.class)
    @ApiResponses(value = @ApiResponse(code = 400, message = "Bad Request, Dates are not available or invalid booking values"))
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public BookingDTO createBooking(@RequestBody @Valid final BookingDTO booking) throws UnavailableDatesException {
        if (booking.getId() != 0L)
            booking.setId(0L);
        Booking rBooking = bookingService.create(bookingConverter.toBooking(booking));
        return bookingConverter.toBookingDTO(rBooking);

    }

    @ApiOperation(value = "Update a Booking", response = BookingDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Booking not found"),
            @ApiResponse(code = 400, message = "Bad Request, Dates are not available or invalid booking values")})
    @RequestMapping(value = "/{id}", method = PUT)
    public BookingDTO updateBooking(@PathVariable Long id, @RequestBody @Valid final BookingDTO booking) throws UnavailableDatesException, BookingNotFoundException {
        if (id != booking.getId()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return bookingConverter.toBookingDTO(bookingService.updateBooking(bookingConverter.toBooking(booking)));
    }

    @ApiOperation(value = "Delete a Booking")
    @ApiResponses(value = @ApiResponse(code = 404, message = "Booking not found"))
    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) throws BookingNotFoundException {
        bookingService.deleteBooking(id);
    }

    // Exception Handling

    @ExceptionHandler(value = BookingNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleBookingNotFoundException(BookingNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(String.format("Booking with ID %s does not exists", e.getId()), NOT_FOUND), NOT_FOUND);
    }

    @ExceptionHandler(value = UnavailableDatesException.class)
    private ResponseEntity<ErrorResponse> handleUnavailableDatesException(UnavailableDatesException e) {
        return new ResponseEntity<>( new ErrorResponse("A booking already exists for the supplied dates", CONFLICT), CONFLICT);
    }

    @ExceptionHandler(value = InvalidBookingLengthException.class)
    private ResponseEntity<ErrorResponse> hanldeInvalidBookingLengthException(InvalidBookingLengthException e) {
        return new ResponseEntity<>( new ErrorResponse("The Campsite needs to be reserved between 1 and 3 days", BAD_REQUEST), BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidBookingRangeException.class)
    private ResponseEntity<Object> handleInvalidBookingRangeException(InvalidBookingRangeException e) {
        return new ResponseEntity<>( new ErrorResponse("The Campsite needs to be reserved at least than one day in advance and no more than 1 month in advance", BAD_REQUEST), BAD_REQUEST);
    }


}
