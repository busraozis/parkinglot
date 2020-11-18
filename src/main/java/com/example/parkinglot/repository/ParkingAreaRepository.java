package com.example.parkinglot.repository;

import com.example.parkinglot.entity.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Integer> {
    @Override
    <S extends ParkingArea> S save(S s);

    ParkingArea findByName(String name);
}
