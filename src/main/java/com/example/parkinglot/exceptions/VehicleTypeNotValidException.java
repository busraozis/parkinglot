package com.example.parkinglot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VehicleTypeNotValidException extends RuntimeException{
    public VehicleTypeNotValidException(String param){
        super(param);
    }
}
