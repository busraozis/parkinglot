package com.example.parkinglot.service.impl;

import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Price;
import com.example.parkinglot.repository.ParkingAreaRepository;
import com.example.parkinglot.repository.PriceRepository;
import com.example.parkinglot.service.IParkService;
import com.example.parkinglot.service.IParkingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    @Transactional
    public ParkingArea saveParkingArea(ParkingArea parkingArea){
        ParkingArea p = parkingAreaRepository.save(parkingArea);
        List<Price> prices = p.getPrices();
        int parkingAreaId = p.getId();
        if(prices != null) {
            for (Price price : prices) {
                price.setParkingAreaId(parkingAreaId);
                priceRepository.save(price);
            }
        }
        return p;

    }

    @Override
    @Transactional
    public void deleteParkingArea(Integer id){
        parkingAreaRepository.deleteById(id);
        priceRepository.deleteAllByParkingAreaId(id);
    }

    @Override
    public ParkingArea getParkingAreaByName(String name){
        ParkingArea p = parkingAreaRepository.findByName(name);
        int parkingAreaId = p.getId();
        List<Price> prices = priceRepository.findAllByParkingAreaId(parkingAreaId);
        p.setPrices(prices);
        return p;
    }

    @Override
    public double getDailyIncome(int id, String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = formatter.parse(date);
        List<Park> parks = iParkService.findAllByCheckOut_Date(dateTime);
        double income = 0;
        for(Park park : parks){
            if(park.getParkingAreaId() == id)
                income += park.getFee();
        }
        return income;
    }

    @Override
    public ParkingArea findById(Integer id){
        Optional<ParkingArea> pa = parkingAreaRepository.findById(id);
        ParkingArea parkingArea = null;
        if(!pa.equals(Optional.empty())){
            parkingArea = pa.get();
            List<Price> prices = priceRepository.findAllByParkingAreaId(pa.get().getId());
            parkingArea.setPrices(prices);
        }
        return parkingArea;
    }

}
