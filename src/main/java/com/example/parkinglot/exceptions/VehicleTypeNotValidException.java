package com.example.parkinglot.exceptions;

public class VehicleTypeNotValidException extends RuntimeException{
    public VehicleTypeNotValidException(String param){
        super(param);
    }
}
