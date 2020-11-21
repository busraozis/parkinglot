package com.example.parkinglot.service.impl;


import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.repository.VehicleRepository;
import com.example.parkinglot.service.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

import static java.util.Optional.*;

@Service
public class VehicleService implements IVehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    Logger logger = LoggerFactory.getLogger(VehicleService.class);

    /**
     *
     * @param vehicle
     * @return Vehicle object
     * @description taked a vehicle as input and searches for Vehicle record with input's plate,
     * if not found, it creates a related Vehicle record,
     * returns created Vehicle object.
     *
     */
    public Vehicle createVehicle(Vehicle vehicle){
        logger.info("createVehicle method started.");
        Optional<Vehicle> v = findByPlate(vehicle.getPlate());
        if(v.equals(empty()))
            return vehicleRepository.save(vehicle);
        else{
            //throw object exists exception
            return null;
        }
    }

    /**
     *
     * @param id
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
     * @param plate
     * @return Optional<Vehicle>
     * @description searches for a Vehicle with input plate.
     *
     */
    public Optional<Vehicle> findByPlate(String plate){
        logger.info("VehicleService.findByPlate method started.");
        return vehicleRepository.findByPlate(plate);
    }
}
