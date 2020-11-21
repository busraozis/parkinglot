package com.example.parkinglot.service;

import com.example.parkinglot.entity.ParkingArea;

import java.text.ParseException;

public interface IParkingAreaService {


    ParkingArea saveParkingArea(ParkingArea parkingArea);

    void deleteParkingArea(Integer id);

    ParkingArea getParkingAreaByName(String name);

    double getDailyIncome(int id, String date) throws ParseException;

    ParkingArea findById(Integer id);
}
