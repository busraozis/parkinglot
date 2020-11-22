package com.example.parkinglot.exceptions;

public class AlreadyCheckedInException extends RuntimeException {
    public AlreadyCheckedInException(String param){
        super(param);
    }
}
