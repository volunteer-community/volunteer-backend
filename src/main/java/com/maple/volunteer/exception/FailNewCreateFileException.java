package com.maple.volunteer.exception;

import com.maple.volunteer.type.ErrorCode;

public class FailNewCreateFileException extends RuntimeException {
    public FailNewCreateFileException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
