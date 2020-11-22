package com.example.parkinglot.dto;

import com.example.parkinglot.entity.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDto{

    private Integer id;
    private Integer parkingAreaId;
    private long startHour;
    private long endHour;
    private double value;


    public Price convertToPriceEntity(){
        return new Price(this.id, this.parkingAreaId, this.startHour,this.endHour, this.value);
    }
}
