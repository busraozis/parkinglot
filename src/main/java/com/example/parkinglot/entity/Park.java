package com.example.parkinglot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@ApiModel(value = "Park", description = "This is a Park model.")
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Vehicle vehicle;

    public Park(Date checkIn, Date checkOut, Integer vehicleId, Integer parkingAreaId, double fee){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.vehicleId = vehicleId;
        this.parkingAreaId = parkingAreaId;
        this.fee = fee;
    }
}
