package com.example.parkinglot.service;

import com.example.parkinglot.entity.Vehicle;

import java.util.Optional;

public interface IVehicleService {

    Vehicle createVehicle(Vehicle vehicle);

    Optional<Vehicle> findById(Integer id);

    Optional<Vehicle> findByPlate(String plate);
}
