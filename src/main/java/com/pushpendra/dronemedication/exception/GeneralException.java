package com.pushpendra.dronemedication.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GeneralException extends Exception{
    private HttpStatus httpStatus;
    private String errorMsg;

}