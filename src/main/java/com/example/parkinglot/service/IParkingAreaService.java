package com.example.parkinglot.service;

import com.example.parkinglot.entity.ParkingArea;

import java.time.LocalDate;
import java.util.Optional;

public interface IParkingAreaService {


    public ParkingArea saveParkingArea(ParkingArea parkingArea);

    public void deleteParkingArea(Integer id);

    public ParkingArea getParkingAreaByName(String name);

    public int getDailyIncome(int id, LocalDate date);

    public ParkingArea findById(Integer id);
}
