package com.example.parkinglot.controller;


import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.service.IParkService;
import com.example.parkinglot.service.IParkingAreaService;
import com.example.parkinglot.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
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
    public ParkingArea updateParkingArea(@RequestBody ParkingArea parkingArea){
        return iParkingAreaService.saveParkingArea(parkingArea);
    }

    @DeleteMapping(value = "/deleteParkingArea/{id}")
    @ApiOperation(value = "Deletes Parking Area By Taking Its Id")
    public void deleteParkingArea(@PathVariable Integer id){
        iParkingAreaService.deleteParkingArea(id);
    }

    @GetMapping(value = "/getParkingAreaByName/{name}")
    @ApiOperation(value = "Get Parking Area By Name", httpMethod = "GET")
    public ParkingArea getParkingAreaByName(@PathVariable @ApiParam String name){
        return iParkingAreaService.getParkingAreaByName(name);
    }

    @PostMapping(value = "/createVehicle")
    @ApiOperation(value = "Creates Vehicle")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle){
        return iVehicleService.createVehicle(vehicle);
    }

    @PostMapping(value = "/checkin")
    @ApiOperation(value = "Checks In a Vehicle in a Parking area", notes = "checkIn, parkingAreaId and vehicle is required.")
    public Park createPark(@RequestBody Park park){
        return iParkService.checkIn(park.getVehicle(),park.getParkingAreaId(),park.getCheckIn());
    }

    @PostMapping(value = "/checkout")
    @ApiOperation(value = "Checks Out a Vehicle From a Parking area", notes = "checkOut, parkId is required.")
    public Park updatePark(@RequestBody Park park){
        return iParkService.checkOut(park);
    }

    @RequestMapping(path = "/getDailyIncome/parkingArea/{id}/date/{date}", method = RequestMethod.GET)
    @ResponseBody
    public double getDailyIncome(@PathVariable Integer id, @PathVariable("date") String date) throws ParseException {
        return iParkingAreaService.getDailyIncome(id,date);
    }

}
