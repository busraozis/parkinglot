package com.example.parkinglot.service;

import com.example.parkinglot.entity.ParkingArea;

import java.text.ParseException;

public interface IParkingAreaService {


    public ParkingArea saveParkingArea(ParkingArea parkingArea);

    public void deleteParkingArea(Integer id);

    public ParkingArea getParkingAreaByName(String name);

    public double getDailyIncome(int id, String date) throws ParseException;

    public ParkingArea findById(Integer id);
}
