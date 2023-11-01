package com.maple.volunteer.exception;

import com.maple.volunteer.type.ErrorCode;

public class NotFoundException extends RuntimeException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
