package com.app.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionMessage {

    private ExceptionCode exceptionCode;
    private String message;


}
