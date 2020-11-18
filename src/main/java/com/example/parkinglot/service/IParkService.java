package com.example.parkinglot.service;

import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.Vehicle;

import java.time.LocalDateTime;

public interface IParkService {

    public Park checkIn(Vehicle vehicle, int parkingAreaId, LocalDateTime date);

    public Park checkOut(Park park);

}
