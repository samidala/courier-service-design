package com.techdisqus.courier.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Response<T> {
    private T result;
    private ErrorCode errorCode;

    public enum ErrorCode{
        INVALID_WEIGHT("invalid weight","001"),
        INVALID_DISTANCE("invalid distance","002"),
        INVALID_PACKAGE_ID("invalid package ID","003"),
        INVALID_OFFER_CODE("invalid offer code","004");

        private final String errorMessage;
        private final String errorCode;
        ErrorCode(String errorMessage, String errorCode) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
