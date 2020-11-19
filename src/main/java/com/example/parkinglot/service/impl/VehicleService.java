package com.example.parkinglot.service.impl;


import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.repository.VehicleRepository;
import com.example.parkinglot.service.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

import static java.util.Optional.*;

@Service
public class VehicleService implements IVehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    public Vehicle createVehicle(Vehicle vehicle){
        Optional<Vehicle> v = findByPlate(vehicle.getPlate());
        if(v.equals(empty()))
            return vehicleRepository.save(vehicle);
        else{
            //throw object exists exception
            return null;
        }
    }

    public Optional<Vehicle> findById(Integer id){
        return vehicleRepository.findById(id);
    }

    public Optional<Vehicle> findByPlate(String plate){
        return vehicleRepository.findByPlate(plate);
    }
}
