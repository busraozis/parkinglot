package com.example.parkinglot.controller;


import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.service.IParkService;
import com.example.parkinglot.service.IParkingAreaService;
import com.example.parkinglot.service.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/parkingLot")
public class ParkingLotController {

    @Autowired
    IParkingAreaService iParkingAreaService;

    @Autowired
    IVehicleService iVehicleService;

    @Autowired
    IParkService iParkService;

    @PostMapping(value = "/createParkingArea")
    public ParkingArea createParkingArea(@RequestBody ParkingArea parkingArea){
        return iParkingAreaService.saveParkingArea(parkingArea);
    }

    @PostMapping(value = "/updateParkingArea")
    public ParkingArea updateParkingArea(@RequestBody ParkingArea parkingArea){
        return iParkingAreaService.saveParkingArea(parkingArea);
    }

    @DeleteMapping(value = "/deleteParkingArea/{id}")
    public void deleteParkingArea(@PathVariable Integer id){
        iParkingAreaService.deleteParkingArea(id);
    }

    @GetMapping(value = "/getParkingAreaByName/{name}")
    public ParkingArea getParkingAreaByName(@PathVariable String name){
        return iParkingAreaService.getParkingAreaByName(name);
    }

    @PostMapping(value = "/createVehicle")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle){
        return iVehicleService.createVehicle(vehicle);
    }

    @PostMapping(value = "/checkin")
    public Park createPark(@RequestBody Park park){
        return iParkService.checkIn(park.getVehicle(),park.getParkingAreaId(),park.getCheckIn());
    }

    @PostMapping(value = "/checkout")
    public Park updatePark(@RequestBody Park park){
        return iParkService.checkOut(park);
    }

    @GetMapping(value = "/getDailyIncome?parkingAreaId={id}&date={date}")
    public int getDailyIncome(@PathVariable Integer id, @PathVariable LocalDateTime date){

        return 0;
    }

}
