package com.example.parkinglot.repository;

import com.example.parkinglot.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ParkRepository extends JpaRepository<Park, Integer> {

    List<Park> findAllByCheckOutBetween(Date startDate, Date endDate);

    Park findByVehicleId(Integer vehicleId);

}
