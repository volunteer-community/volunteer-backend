package com.maple.volunteer.exception;

import com.maple.volunteer.type.ErrorCode;

public class UploadException extends RuntimeException {
    public UploadException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
