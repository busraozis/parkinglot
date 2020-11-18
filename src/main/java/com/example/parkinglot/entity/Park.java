package com.example.parkinglot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Integer vehicleId;
    private Integer parkingAreaId;
    private double fee;
    @Transient
    private Vehicle vehicle;

    public Park(LocalDateTime checkIn, LocalDateTime checkOut, Integer vehicleId, Integer parkingAreaId, double fee){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.vehicleId = vehicleId;
        this.parkingAreaId = parkingAreaId;
        this.fee = fee;
    }
}
