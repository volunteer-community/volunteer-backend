package com.maple.volunteer.exception;


import com.maple.volunteer.type.ErrorCode;

public class CommunityUpdateException extends RuntimeException {
    public CommunityUpdateException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
