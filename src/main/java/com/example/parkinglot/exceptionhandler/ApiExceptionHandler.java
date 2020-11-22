package com.example.parkinglot.exceptionhandler;


import com.example.parkinglot.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({VehicleTypeNotValidException.class})
    public ResponseEntity<String> vehicleTypeNotValidException() {
        return new ResponseEntity<>("Vehicle type is not valid.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PriceListNotValidException.class})
    public ResponseEntity<String> priceListNotValidException() {
        return new ResponseEntity<>("Price List is invalid.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CheckOutTimeLessThanCheckInTimeException.class})
    public ResponseEntity<String> checkOutTimeLessThanCheckInTimeException() {
        return new ResponseEntity<>("Check Out time cannot be less than Check In time.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AlreadyCheckedOutException.class})
    public ResponseEntity<String> alreadyCheckedOutException() {
        return new ResponseEntity<>("Vehicle is already checked out from parking area.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AlreadyCheckedInException.class})
    public ResponseEntity<String> alreadyCheckedInException() {
        return new ResponseEntity<>("Vehicle is already checked in.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ParkingAreaNotFound.class})
    public ResponseEntity<String> parkingAreaNotFound() {
        return new ResponseEntity<>("ParkingArea is not found.", HttpStatus.BAD_REQUEST);
    }
}