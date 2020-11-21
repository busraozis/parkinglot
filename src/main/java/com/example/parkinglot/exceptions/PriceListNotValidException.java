package com.example.parkinglot.exceptions;

public class PriceListNotValidException extends RuntimeException{
    public PriceListNotValidException(String param){
        super(param);
    }
}
