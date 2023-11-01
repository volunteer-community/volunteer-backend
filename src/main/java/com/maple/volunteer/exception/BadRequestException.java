package com.maple.volunteer.exception;

import com.maple.volunteer.type.ErrorCode;

public class BadRequestException extends RuntimeException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
