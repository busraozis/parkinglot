package com.example.parkinglot.service.impl;

import com.example.parkinglot.entity.*;
import com.example.parkinglot.repository.ParkRepository;
import com.example.parkinglot.service.IParkService;
import com.example.parkinglot.service.IParkingAreaService;
import com.example.parkinglot.service.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static jdk.nashorn.internal.objects.NativeMath.round;

@Service
public class ParkService implements IParkService {

    @Autowired
    IVehicleService iVehicleService;

    @Autowired
    ParkRepository parkRepository;

    @Autowired
    IParkingAreaService iParkingAreaService;

    public static final double SUV_MULTIPLIER = 1.10;
    public static final double MINIVAN_MULTIPLIER = 1.15;

    Logger logger = LoggerFactory.getLogger(ParkService.class);

    @Override
    @Transactional
    public Park checkIn(Vehicle vehicle, int parkingAreaId, Date date){

        Optional<Vehicle> v = iVehicleService.findByPlate(vehicle.getPlate());
        Park park = null;
        if(v.equals(Optional.empty())){
            vehicle = iVehicleService.createVehicle(vehicle);
        }else{
            vehicle = v.get();
        }
        try {
            ParkingArea p = iParkingAreaService.findById(parkingAreaId);
            int vehicleCount = p.getVehicleCount();
            if(vehicleCount + 1 <= p.getCapacity()) {
                park = new Park(date, null, vehicle.getId(), p.getId(), 0);
                park = parkRepository.save(park);
                p.setVehicleCount(vehicleCount + 1);
                iParkingAreaService.saveParkingArea(p);
            }else{
                logger.info("Park cannot be created.");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw e;
        }

        return park;
    }

    @Override
    @Transactional
    public Park checkOut(Park park){
        Park p = parkRepository.findById(park.getId()).get();
        p.setCheckOut(park.getCheckOut());
        double fee = calculateFee(p.getVehicleId(),p.getParkingAreaId(), p.getCheckIn(), p.getCheckOut());
        p.setFee(fee);

        ParkingArea pa = iParkingAreaService.findById(p.getParkingAreaId());
        int vehicleCount = pa.getVehicleCount();
        pa.setVehicleCount(vehicleCount - 1);
        iParkingAreaService.saveParkingArea(pa);
        return parkRepository.save(p);
    }

    public double calculateFee(int vehicleId, int parkingAreaId, Date checkInTime, Date checkOutTime){
        ParkingArea pa = iParkingAreaService.findById(parkingAreaId);
        List<Price> priceList = pa.getPrices();
        Vehicle v = iVehicleService.findById(vehicleId).get();

        long timeDifference = timeDifferenceInHours(checkInTime,checkOutTime);

        double priceValue = 0;

        for(Price price : priceList){
            long startHour = price.getStartHour();
            long endHour = price.getEndHour();
            if(timeDifference >= startHour && timeDifference < endHour){
                priceValue = price.getValue();
                break;
            }
        }

        //If vehicle is of type Suv, then price is price times SUV_MULTIPLIER
        if(v.getType().equals(Type.SUV))
            priceValue = priceValue * SUV_MULTIPLIER;

        //If vehicle is of type Minivan, then price is price times MINIVAN_MULTIPLIER
        if(v.getType().equals(Type.MINIVAN))
            priceValue = priceValue * MINIVAN_MULTIPLIER;

        return Math.round(priceValue * 100.0) / 100.0;
    }

    public static long timeDifferenceInHours(Date date1, Date date2){

        long diff = (date2.getTime() - date1.getTime()) / (60 * 60 * 1000);
        return diff;
    }

    @Override
    public List<Park> findAllByCheckOut_Date(Date date){
        Date endDate = new Date(date.getTime() + (60 * 60 * 24 * 1000));
        return parkRepository.findAllByCheckOutBetween(date, endDate);
    }
}
