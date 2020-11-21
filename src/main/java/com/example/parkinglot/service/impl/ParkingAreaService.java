package com.example.parkinglot.service.impl;

import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Price;
import com.example.parkinglot.repository.ParkingAreaRepository;
import com.example.parkinglot.repository.PriceRepository;
import com.example.parkinglot.service.IParkService;
import com.example.parkinglot.service.IParkingAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingAreaService implements IParkingAreaService {

    @Autowired
    ParkingAreaRepository parkingAreaRepository;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    IParkService iParkService;

    Logger logger = LoggerFactory.getLogger(ParkingAreaService.class);

    /**
     *
     * @param parkingArea : ParkingArea object to be saved.
     * @return p of type ParkingArea
     * @description takes a Parking area object and saves it to the db,
     * also taking a list of parking area prices and saves it to the db as well.
     */
    @Override
    @Transactional
    public ParkingArea saveParkingArea(ParkingArea parkingArea){
        ParkingArea p = parkingAreaRepository.save(parkingArea);
        logger.info("ParkingArea {},{}" , p.getId() , " is saved.");
        List<Price> prices = p.getPrices();
        int parkingAreaId = p.getId();
        if(prices != null) {
            for (Price price : prices) {
                price.setParkingAreaId(parkingAreaId);
                Price pr = priceRepository.save(price);
                logger.info("Price {},{}" , pr.getId() , " is saved.");
            }
        }
        return p;

    }

    /**
     *
     * @param id : id of ParkingArea record to be deleted.
     * @description takes id of a parkingArea record and deletes corresponding
     * ParkingArea and Price records in the db.
     *
     */
    @Override
    @Transactional
    public void deleteParkingArea(Integer id){
        parkingAreaRepository.deleteById(id);
        logger.info("ParkingArea  {},{}" , id , "is deleted.");
        priceRepository.deleteAllByParkingAreaId(id);
        logger.info("All Price related to ParkingArea are deleted.");
    }

    /**
     *
     * @param name : name of ParkingArea record to be retrieved.
     * @return p of type ParkingArea
     * @description takes a name parameter and finds ParkingArea record with name
     * and finds corresponding Price records of this found ParkingArea record by its id,
     * sets price list of found ParkingArea record to these Price records and then
     * returns this found ParkingArea object.
     *
     */
    @Override
    public ParkingArea getParkingAreaByName(String name){
        ParkingArea p = parkingAreaRepository.findByName(name);
        logger.info("ParkingArea with name  {},{}" , name , " is found.");
        int parkingAreaId = p.getId();
        List<Price> prices = priceRepository.findAllByParkingAreaId(parkingAreaId);
        p.setPrices(prices);
        logger.info("All Price related to ParkingArea with name  {},{}" , name , " is found.");
        return p;
    }

    /**
     *
     * @param id : id of ParkingArea record daily income of which will be calculated.
     * @param date : date of income to be calculated.
     * @return income of type double
     * @throws ParseException : throws ParseException when String cannot be converted o Date.
     * @description takes id of a ParkingArea and a date as string,
     * converts input String date to a Date object,
     * finds all Park records that were checked out on the input date,
     * for each Park record, it compares its parkingAreaId with the input id
     * and if they match, it adds the Park fee to the income,
     * returns this income.
     *
     */
    @Override
    public double getDailyIncome(int id, String date) throws ParseException {
        logger.info("getDailyIncome method started.");
        logger.info("Input Date: {}" , date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = formatter.parse(date);
        List<Park> parks = iParkService.findAllByCheckOutDate(dateTime);
        logger.info("Park records are found.");
        double income = 0;
        for(Park park : parks){
            if(park.getParkingAreaId() == id)
                income += park.getFee();
        }
        return income;
    }

    /**
     *
     * @param id : id of ParkingArea record to be found.
     * @return parkingArea of type ParkingArea
     * @description takes an id of a ParkingArea,
     * searches for it in the db,
     * if it finds, searches for related Price list in the Price table with parkingAreaId,
     * sets this price list to parkingArea,
     * returns found parkingArea.
     *
     */
    @Override
    public ParkingArea findById(Integer id){
        logger.info("ParkingAreaService.findById method started.");
        Optional<ParkingArea> pa = parkingAreaRepository.findById(id);
        ParkingArea parkingArea = null;
        if(!pa.equals(Optional.empty()) && pa.isPresent()){
            parkingArea = pa.get();
            logger.info("ParkingArea is found.");
            List<Price> prices = priceRepository.findAllByParkingAreaId(pa.get().getId());
            parkingArea.setPrices(prices);
        }
        return parkingArea;
    }

}
