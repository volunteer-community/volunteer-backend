package com.maple.volunteer.exception;

import com.maple.volunteer.type.ErrorCode;

public class CommunityRecruitmentException extends RuntimeException {
    public CommunityRecruitmentException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
