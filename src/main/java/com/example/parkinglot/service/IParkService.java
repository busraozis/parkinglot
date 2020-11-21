package com.example.parkinglot.service;

import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.Vehicle;

import java.util.Date;
import java.util.List;

public interface IParkService {

    Park checkIn(Vehicle vehicle, int parkingAreaId, Date date);

    Park checkOut(Park park);

    List<Park> findAllByCheckOutDate(Date date);

}
