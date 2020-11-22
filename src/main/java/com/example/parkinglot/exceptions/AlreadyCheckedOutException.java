package com.example.parkinglot.exceptions;

public class AlreadyCheckedOutException extends RuntimeException {
    public AlreadyCheckedOutException(String param){
        super(param);
    }
}
