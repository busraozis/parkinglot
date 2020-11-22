package com.example.parkinglot.dto;

import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingAreaDto {

    private Integer id;

    private String name;
    private int capacity;
    private String city;
    private int vehicleCount;
    @Transient
    private List<PriceDto> prices;

    public ParkingArea convertToParkingAreaEntity(){
        List<Price> priceList = new ArrayList<>();
        for(PriceDto price : this.prices)
            priceList.add(price.convertToPriceEntity());

        return new ParkingArea(this.id, this.name,this.capacity, this.city,this.vehicleCount,priceList);

    }

}
