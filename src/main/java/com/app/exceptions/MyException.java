package com.app.exceptions;

import lombok.Getter;

@Getter
public class MyException extends RuntimeException {

    private ExceptionMessage exceptionMessage;

    public MyException(ExceptionCode exceptionCode, String message) {
        this.exceptionMessage = new ExceptionMessage(exceptionCode, message);
    }

}
