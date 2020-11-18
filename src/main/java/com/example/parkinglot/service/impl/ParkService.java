package com.example.parkinglot.service.impl;

import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Price;
import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.repository.ParkRepository;
import com.example.parkinglot.service.IParkService;
import com.example.parkinglot.service.IParkingAreaService;
import com.example.parkinglot.service.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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
    public Park checkIn(Vehicle vehicle, int parkingAreaId, LocalDateTime date){

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
    public Park checkOut(Park park){
        Park p = parkRepository.findById(park.getId()).get();
        p.setCheckOut(park.getCheckOut());
        double fee = calculateFee(p.getVehicleId(),p.getParkingAreaId(), p.getCheckIn(), p.getCheckOut());
        p.setFee(fee);
        return parkRepository.save(p);
    }

    public double calculateFee(int vehicleId, int parkingAreaId, LocalDateTime checkInTime, LocalDateTime checkOutTime){
        ParkingArea pa = iParkingAreaService.findById(parkingAreaId);
        List<Price> priceList = pa.getPrices();
        Vehicle v = iVehicleService.findById(vehicleId).get();

        long timeDifference = timeDifferenceInHours(checkInTime,checkOutTime);

        int priceValue = 0;

        for(Price price : priceList){
            long startHour = price.getStartHour();
            long endHour = price.getEndHour();
            if(timeDifference >= startHour && timeDifference < endHour){
                priceValue = price.getValue();
                break;
            }
        }

        //If vehicle is of type Suv, then price is price times SUV_MULTIPLIER

        //If vehicle is of type Minivan, then price is price times MINIVAN_MULTIPLIER


        return priceValue;
    }

    public static long timeDifferenceInHours(LocalDateTime date1, LocalDateTime date2){

        long diff = date1.until(date2, ChronoUnit.HOURS);
        return diff;
    }
}
