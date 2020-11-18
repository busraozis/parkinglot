package com.example.parkinglot.repository;

import com.example.parkinglot.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkRepository extends JpaRepository<Park, Integer> {
}
