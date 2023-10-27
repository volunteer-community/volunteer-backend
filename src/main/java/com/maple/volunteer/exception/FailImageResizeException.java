package com.maple.volunteer.exception;

import com.maple.volunteer.type.ErrorCode;

public class FailImageResizeException extends RuntimeException {
    public FailImageResizeException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
