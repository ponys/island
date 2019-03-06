package com.plugli;

import com.plugli.model.Booking;
import com.plugli.model.BookingDate;
import com.plugli.model.dto.BookingDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    public static Booking createBooking(List<LocalDate> days) {
        Booking booking = new Booking();
        booking.setName("user");
        booking.setEmail("user@test.com");
        for (LocalDate day : days) {
            booking.addBookingDate(new BookingDate(day));
        }
        return booking;
    }

    public static BookingDTO createBookingDTO(LocalDate arrivalDate, LocalDate departureDate) {
        BookingDTO booking = new BookingDTO();
        booking.setName("user");
        booking.setEmail("user@test.com");
        booking.setArrivalDate(arrivalDate);
        booking.setDepartureDate(departureDate);
        return booking;
    }

    public static String asJsonString(BookingDTO booking) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (booking.getId() != 0L) {
            sb.append("\"id\":\"").append(booking.getId()).append("\",");
        }
        sb.append("\"name\":\"").append(booking.getName()).append("\",")
                .append("\"email\":\"").append(booking.getEmail()).append("\",")
                .append("\"departureDate\":\"").append(booking.getDepartureDate().toString()).append("\",")
                .append("\"arrivalDate\":\"").append(booking.getArrivalDate().toString()).append("\"")
                .append("}");
        return sb.toString();
    }


    public static List<LocalDate> tomorrow() {
        return Collections.singletonList(LocalDate.now().plusDays(1));
    }

}
