package com.plugli.service.exception;

public class BookingNotFoundException extends Exception {

    private long id;

    public BookingNotFoundException(long id) {
        super();
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
