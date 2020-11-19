package com.example.parkinglot.service;

import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IParkService {

    public Park checkIn(Vehicle vehicle, int parkingAreaId, Date date);

    public Park checkOut(Park park);

    public List<Park> findAllByCheckOut_Date(Date date);

}
