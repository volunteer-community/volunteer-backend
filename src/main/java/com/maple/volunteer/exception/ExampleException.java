package com.maple.volunteer.exception;


import com.maple.volunteer.type.ErrorCode;

public class ExampleException extends RuntimeException {
    public ExampleException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
