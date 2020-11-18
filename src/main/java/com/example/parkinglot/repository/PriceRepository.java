package com.example.parkinglot.repository;

import com.example.parkinglot.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    Integer deleteAllByParkingAreaId(Integer parkingAreaId);

    List<Price> findAllByParkingAreaId(Integer parkingAreaId);
}
