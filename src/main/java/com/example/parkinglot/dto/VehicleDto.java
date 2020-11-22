package com.example.parkinglot.dto;

import com.example.parkinglot.entity.Type;
import com.example.parkinglot.entity.Vehicle;
import lombok.Data;

@Data
public class VehicleDto {

    private Integer id;
    private String plate;
    private Type type;

    public Vehicle convertToVehicleEntity(){
        return new Vehicle(this.id, this.plate,this.type);
    }
}
