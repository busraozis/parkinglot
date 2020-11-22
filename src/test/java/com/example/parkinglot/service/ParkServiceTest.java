package com.example.parkinglot.service;

import com.example.parkinglot.entity.*;
import com.example.parkinglot.exceptions.AlreadyCheckedOutException;
import com.example.parkinglot.exceptions.CheckOutTimeLessThanCheckInTimeException;
import com.example.parkinglot.repository.ParkRepository;
import com.example.parkinglot.service.impl.ParkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ParkServiceTest {

    @InjectMocks
    ParkService parkService;

    @Mock
    ParkRepository parkRepository;

    @Mock
    IVehicleService iVehicleService;

    @Mock
    IParkingAreaService iParkingAreaService;

    private List<Park> parks;
    public static final double MINIVAN_MULTIPLIER = 1.15;

    @Before
    public void setup() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date checkIn = formatter.parse("2020-11-20 08:25:07");
        Date checkOut = formatter.parse("2020-11-20 18:25:07");

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2020-11-20");
        Date endDate = new Date(date.getTime() + (60 * 60 * 24 * 1000));

        parks = new ArrayList<>();
        Park park = new Park(checkIn,checkOut, 1, 1,15.0);
        parks.add(park);
        parks.add(new Park(checkIn,checkOut, 3, 1,115.0));
        parks.add(new Park(checkIn,checkOut, 5, 2,25.0));
        Mockito.when(parkRepository.findAllByCheckOutBetween(date,endDate)).thenReturn(parks);

        List<Price> prices = new ArrayList<>();
        prices.add(new Price(1, 1, 0, 10, 11));
        prices.add(new Price(1, 1, 10, 24, 18));
        ParkingArea parkingArea = new ParkingArea(1,"Park A", 3, "City 1", 0, prices);

        Vehicle vehicle = new Vehicle(1,"34M01", Type.MINIVAN);

        Vehicle vehicle2 = new Vehicle(2,"34KLM01", Type.SEDAN);
        Mockito.when(iVehicleService.createVehicle(Mockito.any(Vehicle.class))).thenReturn(vehicle2);

        Mockito.when(iVehicleService.findByPlate("34M01")).thenReturn(java.util.Optional.of(vehicle));
        Mockito.when(parkRepository.findByVehicleId(1)).thenReturn(park);
        Mockito.when(iParkingAreaService.findById(1)).thenReturn(parkingArea);
        Mockito.when(iParkingAreaService.saveParkingArea(Mockito.any(ParkingArea.class))).thenReturn(parkingArea);
        Mockito.when(iVehicleService.findById(1)).thenReturn(java.util.Optional.of(vehicle));

    }

    @Test
    public void shouldReturnParksBetweenDateAndDatePlusOneDay() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2020-11-20");

        List<Park> actualParks = parkService.findAllByCheckOutDate(date);
        assertEquals(parks.size(), actualParks.size());
    }

    @Test
    public void shouldReturnParkIfWhenCheckIn() throws ParseException {
        Vehicle v = new Vehicle(1,"34M01", Type.MINIVAN);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date checkIn = formatter.parse("2020-11-20 08:25:07");
        Park park = new Park(checkIn, null, v.getId(), 1, 0);

        Mockito.when(parkRepository.save(Mockito.any(Park.class))).thenReturn(park);
        Mockito.when(iParkingAreaService.saveParkingArea(Mockito.any(ParkingArea.class))).thenReturn(new ParkingArea());

        Park actual = parkService.checkIn(v,1,checkIn);

        assertEquals(checkIn,actual.getCheckIn());

        Vehicle v2 = new Vehicle(2,"34KLM01", Type.SEDAN);

        actual = parkService.checkIn(v2,1,checkIn);
        Mockito.verify(iVehicleService,Mockito.atLeast(1)).createVehicle(v2);
        assertEquals(checkIn,actual.getCheckIn());

    }

    @Test
    public void shouldThrowExceptionWhenCheckOutLessThanCheckIn() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date checkIn = formatter.parse("2020-11-20 08:25:07");
        Date checkOut = formatter.parse("2020-11-20 05:25:07");
        Park park = new Park(checkIn, null, 1, 1, 0);
        Mockito.when(parkRepository.findByVehicleId(1)).thenReturn(park);
        Park park1 = new Park(checkIn, checkOut, 1, 1, 0);
        assertThrows(CheckOutTimeLessThanCheckInTimeException.class, () -> parkService.checkOut(park1));
    }

    @Test
    public void shouldThrowExceptionWhenCheckOutIsNotEmpty() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date checkIn = formatter.parse("2020-11-20 08:25:07");
        Date checkOut = formatter.parse("2020-11-20 18:25:07");
        Park park = new Park(checkIn, checkOut, 1, 1, 0);
        Mockito.when(parkRepository.findByVehicleId(1)).thenReturn(park);
        assertThrows(AlreadyCheckedOutException.class, () -> parkService.checkOut(park));
    }

    @Test
    public void shouldReturnParkWhenCheckedOut() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date checkIn = formatter.parse("2020-11-20 08:25:07");
        Date checkOut = formatter.parse("2020-11-20 15:25:07");
        Park park = new Park(checkIn, null, 1, 1, 0);
        Mockito.when(parkRepository.findByVehicleId(1)).thenReturn(park);
        Park park1 = new Park(null, checkOut, 1, 1, MINIVAN_MULTIPLIER * 11);
        Mockito.when(parkRepository.save(Mockito.any(Park.class))).thenReturn(park1);
        Park actual = parkService.checkOut(park1);
        assertEquals(checkOut , actual.getCheckOut());
        assertEquals(MINIVAN_MULTIPLIER * 11 , actual.getFee());
    }

}
