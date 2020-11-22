package com.example.parkinglot.dto;

import com.example.parkinglot.entity.Park;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@NoArgsConstructor
@ApiModel(value = "Park", description = "This is a Park model.")
public class ParkDto {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "checkInDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkIn;

    @ApiModelProperty(value = "checkOutDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOut;

    @ApiModelProperty(value = "vehicleId")
    private Integer vehicleId;

    @ApiModelProperty(value = "parkingAreaId")
    private Integer parkingAreaId;

    @ApiModelProperty(value = "fee")
    private double fee;

    @ApiModelProperty
    @Transient
    private VehicleDto vehicle;

    public Park convertToParkEntity(){
        return new Park(this.checkIn, this.checkOut, this.vehicleId, this.parkingAreaId, this.fee);
    }
}
