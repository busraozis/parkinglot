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
import java.util.Date;
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

    /**
     *
     * @param vehicle : Vehicle object to be parked.
     * @param parkingAreaId : id of ParkingArea record where the vehicle will be parked.
     * @param date : check in date of vehicle to the parking area
     * @return park of type Park
     * @description takes a Vehicle object to be parked, takes parkingAreaId of the ParkingArea
     * where the vehicle will be parked and a Date as checkInDate of this parking operation.
     * Searches for the vehicle in the db, if vehicle doesn't exist, it stores the vehicle to the db.
     * Finds ParkingArea record with parkingAreaId, if it finds, checks for its remaining vehicle capacity.
     * If capacity is greater than vehicleCount + 1, it creates a new Park record related to ParkingArea
     * and updates adding  +1 to vehicleCount of ParkingArea record.
     * If capacity is exceeded, park is not created.
     * Returns created Park object.
     *
     */
    @Override
    @Transactional
    public Park checkIn(Vehicle vehicle, int parkingAreaId, Date date){

        Optional<Vehicle> v = iVehicleService.findByPlate(vehicle.getPlate());
        Park park = null;
        if(v.equals(Optional.empty())){
            vehicle = iVehicleService.createVehicle(vehicle);
            logger.info("Vehicle did not exist. Vehicle {},{}" , vehicle.getId() , " is created.");
        }else{
            vehicle = v.get();
            logger.info("Vehicle is found.");
        }

        ParkingArea p = iParkingAreaService.findById(parkingAreaId);
        int vehicleCount = p.getVehicleCount();
        if(vehicleCount + 1 <= p.getCapacity()) {
            logger.info("ParkingArea has capacity. Capacity: {},{},{}" , p.getCapacity() , " VehicleCount : " , vehicleCount);
            park = new Park(date, null, vehicle.getId(), p.getId(), 0);
            park = parkRepository.save(park);
            logger.info("Park is created.");
            p.setVehicleCount(vehicleCount + 1);
            iParkingAreaService.saveParkingArea(p);
            logger.info("ParkingArea vehicleCount is updated.");
        }else{
            logger.info("ParkingArea capacity will be exceeded. Capacity: {},{},{}" , p.getCapacity() , " VehicleCount : " , vehicleCount);
            logger.info("Park cannot be created.");
        }
        return park;
    }

    /**
     *
     * @param park : Park object to be checked out.
     * @return updated Park object.
     * @description takes park input to be checked-out,
     * finds the Park record to be checked-out by input park's id,
     * calculates fee and sets fee of found Park object,
     * finds park-related ParkingArea record and updates its vehicle count subtracting 1.
     * returns updated Park object.
     *
     */
    @Override
    @Transactional
    public Park checkOut(Park park){

        Park p = parkRepository.findById(park.getId()).get();
        logger.info("Park {},{}" , park.getId() , "is found.");
        logger.info("CheckOutDate: {}" , park.getCheckOut().toString());
        logger.info("CheckInDate: {}" , p.getCheckIn().toString());
        logger.info("Vehicle Id: {}" , p.getVehicleId());
        p.setCheckOut(park.getCheckOut());
        double fee = calculateFee(p.getVehicleId(),p.getParkingAreaId(), p.getCheckIn(), p.getCheckOut());
        p.setFee(fee);
        logger.info("Fee is calculated.");

        ParkingArea pa = iParkingAreaService.findById(p.getParkingAreaId());
        int vehicleCount = pa.getVehicleCount();
        pa.setVehicleCount(vehicleCount - 1);
        iParkingAreaService.saveParkingArea(pa);
        logger.info("ParkingArea vehicleCount is updated.");
        return parkRepository.save(p);
    }

    /**
     *
     * @param vehicleId : id of Vehicle
     * @param parkingAreaId : id of ParkingArea
     * @param checkInTime : checkin time
     * @param checkOutTime : checkout time
     * @return a double value that represents fee.
     * @description takes vehicleId, parkingAreaId, checkInTime and checkInDate.
     * finds ParkingArea record and its price list by parkingAreaId,
     * finds Vehicle record by vehicleId,
     * calculates time difference in hours between checkInTime and checkOutTime,
     * For each Price in price list, compares timeDifference to startHour and endHour of Price,
     * and if it's between these hours, it takes related value of Price as base priceValue.
     * If vehicle id of type SUV, it multiplies base priceValue with SUV_MULTIPLIER,
     * if vehicle id of type MINIVAN, it multiplies base priceValue with MINIVAN_MULTIPLIER,
     * returns calculated priceValue rounded to 2 decimal places.
     *
     */
    public double calculateFee(int vehicleId, int parkingAreaId, Date checkInTime, Date checkOutTime){
        logger.info("calculateFee started.");
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

        logger.info("calculateFee ended.");
        return Math.round(priceValue * 100.0) / 100.0;
    }

    /**
     *
     * @param date1 : date with less value
     * @param date2 : date with greater value
     * @return a long value
     * @description calculates time difference in hours between two Date objects.
     *
     */
    public static long timeDifferenceInHours(Date date1, Date date2){
        return (date2.getTime() - date1.getTime()) / (60 * 60 * 1000);
    }

    /**
     *
     * @param date : date
     * @return List of Park objects
     * @description finds all Park records checkOutDate of which are in between input Date
     * and input Date + 1 day.
     *
     */
    @Override
    public List<Park> findAllByCheckOutDate(Date date){

        logger.info("findAllByCheckOut_Date method started.");
        Date endDate = new Date(date.getTime() + (60 * 60 * 24 * 1000));
        return parkRepository.findAllByCheckOutBetween(date, endDate);
    }
}
