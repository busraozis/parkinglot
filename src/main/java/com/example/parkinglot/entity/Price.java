package com.example.parkinglot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price implements Comparable<Price>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer parkingAreaId;
    private long startHour;
    private long endHour;
    private double value;

    @Override
    public int compareTo(Price p){
        Long startHour = new Long(this.startHour);
        Long pStartHour = new Long(p.getStartHour());
        return startHour.compareTo(pStartHour);
    }
}
