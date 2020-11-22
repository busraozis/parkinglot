package com.example.parkinglot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PriceListNotValidException extends RuntimeException{
    public PriceListNotValidException(String param){
        super(param);
    }
}
