package com.example.parkinglot.repository;

import com.example.parkinglot.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle,Integer>{

    Optional<Vehicle> findByPlate(String plate);
}
