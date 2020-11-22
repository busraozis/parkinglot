package com.example.parkinglot.service.impl;


import com.example.parkinglot.entity.Type;
import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.exceptions.VehicleTypeNotValidException;
import com.example.parkinglot.repository.VehicleRepository;
import com.example.parkinglot.service.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.*;

@Service
public class VehicleService implements IVehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    Logger logger = LoggerFactory.getLogger(VehicleService.class);

    /**
     *
     * @param vehicle : Vehicle object to be created.
     * @return Vehicle object
     * @description takes a vehicle as input and searches for Vehicle record with input's plate,
     * if not found, it creates a related Vehicle record,
     * returns created Vehicle object.
     *
     */
    public Vehicle createVehicle(Vehicle vehicle){
        logger.info("createVehicle method started.");
        if(!(vehicle.getType().equals(Type.SEDAN) || vehicle.getType().equals(Type.SUV) || vehicle.getType().equals(Type.MINIVAN)))
            throw new VehicleTypeNotValidException(vehicle.getType().toString());

        Optional<Vehicle> v = findByPlate(vehicle.getPlate());
        if(v.equals(empty()))
            return vehicleRepository.save(vehicle);
        else{
            logger.info("Vehicle to be created already exists.");
            return null;
        }
    }

    /**
     *
     * @param id : id of Vehicle to be retrieved.
     * @return Optional<Vehicle>
     * @description searches for a Vehicle with input id.
     *
     */
    public Optional<Vehicle> findById(Integer id){
        logger.info("VehicleService.findById method started.");
        return vehicleRepository.findById(id);
    }

    /**
     *
     * @param plate : plate of Vehicle to be retrieved.
     * @return Optional<Vehicle>
     * @description searches for a Vehicle with input plate.
     *
     */
    public Optional<Vehicle> findByPlate(String plate){
        logger.info("VehicleService.findByPlate method started.");
        return vehicleRepository.findByPlate(plate);
    }
}
