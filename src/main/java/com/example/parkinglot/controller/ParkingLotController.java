package com.example.parkinglot.controller;


import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.exceptions.PriceListNotValidException;
import com.example.parkinglot.exceptions.VehicleTypeNotValidException;
import com.example.parkinglot.service.IParkService;
import com.example.parkinglot.service.IParkingAreaService;
import com.example.parkinglot.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/parkingLot")
@Api(value = "Parking Lot API")
public class ParkingLotController {

    @Autowired
    IParkingAreaService iParkingAreaService;

    @Autowired
    IVehicleService iVehicleService;

    @Autowired
    IParkService iParkService;

    @PostMapping(value = "/createParkingArea")
    @ApiOperation(value = "Creates Parking Area", httpMethod = "POST")
    public ResponseEntity<ParkingArea> createParkingArea(@RequestBody @ApiParam ParkingArea parkingArea){
        return new ResponseEntity<>(iParkingAreaService.saveParkingArea(parkingArea), HttpStatus.CREATED);
    }

    @PostMapping(value = "/updateParkingArea")
    @ApiOperation(value = "Updates Parking Area")
    public ResponseEntity<ParkingArea> updateParkingArea(@RequestBody ParkingArea parkingArea){
        return new ResponseEntity<>(iParkingAreaService.saveParkingArea(parkingArea),HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteParkingArea/{id}")
    @ApiOperation(value = "Deletes Parking Area By Taking Its Id")
    public void deleteParkingArea(@PathVariable Integer id){
        iParkingAreaService.deleteParkingArea(id);
    }

    @GetMapping(value = "/getParkingAreaByName/{name}")
    @ApiOperation(value = "Get Parking Area By Name", httpMethod = "GET")
    public ResponseEntity<ParkingArea> getParkingAreaByName(@PathVariable @ApiParam String name){
        return new ResponseEntity<>(iParkingAreaService.getParkingAreaByName(name), HttpStatus.FOUND);
    }

    @PostMapping(value = "/createVehicle")
    @ApiOperation(value = "Creates Vehicle")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle){
        return new ResponseEntity<>(iVehicleService.createVehicle(vehicle),HttpStatus.CREATED);
    }

    @PostMapping(value = "/checkin")
    @ApiOperation(value = "Checks In a Vehicle in a Parking area", notes = "checkIn, parkingAreaId and vehicle is required.")
    public ResponseEntity<Park> createPark(@RequestBody Park park){
        return new ResponseEntity<>(iParkService.checkIn(park.getVehicle(),park.getParkingAreaId(),park.getCheckIn()),HttpStatus.CREATED);
    }

    @PostMapping(value = "/checkout")
    @ApiOperation(value = "Checks Out a Vehicle From a Parking area", notes = "checkOut, parkId is required.")
    public ResponseEntity<Park> updatePark(@RequestBody Park park){
        return new ResponseEntity<>(iParkService.checkOut(park), HttpStatus.OK);
    }

    @GetMapping(path = "/getDailyIncome/parkingArea/{id}/date/{date}")
    @ResponseBody
    @ApiOperation(value = "Gets Daily Income of a ParkingArea on the given date")
    public double getDailyIncome(@PathVariable Integer id, @PathVariable("date") String date) throws ParseException {
        return iParkingAreaService.getDailyIncome(id,date);
    }

    @ExceptionHandler({VehicleTypeNotValidException.class})
    public String vehicleTypeNotValidException(){
        return "Vehicle type is not valid.";
    }

    @ExceptionHandler({PriceListNotValidException.class})
    public String priceListNotValidException(){
        return "Price List is invalid.";
    }
}
