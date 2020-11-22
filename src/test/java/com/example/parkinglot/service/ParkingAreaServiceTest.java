package com.example.parkinglot.service;
import com.example.parkinglot.entity.Park;
import com.example.parkinglot.entity.ParkingArea;
import com.example.parkinglot.entity.Price;
import com.example.parkinglot.exceptions.ParkingAreaNotFound;
import com.example.parkinglot.exceptions.PriceListNotValidException;
import com.example.parkinglot.repository.ParkingAreaRepository;
import com.example.parkinglot.repository.PriceRepository;
import com.example.parkinglot.service.impl.ParkingAreaService;
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
public class ParkingAreaServiceTest {

    @InjectMocks
    ParkingAreaService parkingAreaService;

    @Mock
    ParkingAreaRepository parkingAreaRepository;

    @Mock
    PriceRepository priceRepository;

    @Mock
    IParkService iParkService;

    private ParkingArea pa;

    @Before
    public void setup() throws ParseException {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price(1, 1, 0, 10, 11));
        prices.add(new Price(1, 1, 10, 24, 18));
        Price price = new Price(1, 1, 0, 24, 18);
        pa = new ParkingArea(1, "Park A",50,"City",0 , prices);
        Mockito.when(parkingAreaRepository.save(pa)).thenReturn(pa);
        Mockito.when(priceRepository.save(Mockito.any(Price.class))).thenReturn(price);
        Mockito.when(parkingAreaRepository.findByName("Park A")).thenReturn(pa);
        Mockito.when(parkingAreaRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(pa));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date checkIn = formatter.parse("2020-11-20 08:25:07");
        Date checkOut = formatter.parse("2020-11-20 18:25:07");

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2020-11-20");

        List<Park> parks = new ArrayList<>();
        parks.add(new Park(checkIn,checkOut, 1, 1,15.0));
        parks.add(new Park(checkIn,checkOut, 3, 1,115.0));
        parks.add(new Park(checkIn,checkOut, 5, 2,25.0));
        Mockito.when(iParkService.findAllByCheckOutDate(date)).thenReturn(parks);
    }

    @Test
    public void shouldThrowExceptionIfPriceListInvalidWhenSaveParkingArea(){
        List<Price> priceList = new ArrayList<>();
        priceList.add(new Price(1, 1, 0, 15, 11));
        priceList.add(new Price(1, 1, 10, 24, 11));
        pa.setPrices(priceList);
        assertThrows(PriceListNotValidException.class, () -> parkingAreaService.saveParkingArea(pa));

        priceList.clear();
        priceList.add(new Price(1, 1, 10, 26, 11));
        pa.setPrices(priceList);
        assertThrows(PriceListNotValidException.class, () -> parkingAreaService.saveParkingArea(pa));

    }

    @Test
    public void shouldReturnParkingAreaIfParkingAreaSAvedWhenSaveParkingArea(){
        List<Price> priceList = new ArrayList<>();
        priceList.add(new Price(1, 1, 0, 10, 11));
        priceList.add(new Price(1, 1, 10, 24, 18));
        pa.setPrices(priceList);
        ParkingArea actual = parkingAreaService.saveParkingArea(pa);

        assertEquals(pa.getName(), actual.getName());
        assertEquals(pa.getCapacity(), actual.getCapacity());
        assertEquals(pa.getCity(), actual.getCity());
        assertEquals(pa.getVehicleCount(), actual.getVehicleCount());

    }

    @Test
    public void shouldThrowExceptionIfParkingAreaNotExistsWhenGetByName(){
        assertThrows(ParkingAreaNotFound.class, () -> parkingAreaService.getParkingAreaByName("Park B"));
    }

    @Test
    public void shouldReturnParkingAreaIfParkingAreaExistsWhenGetByName(){
        ParkingArea actual = parkingAreaService.getParkingAreaByName("Park A");

        assertEquals(pa.getName(), actual.getName());
        assertEquals(pa.getCapacity(), actual.getCapacity());
        assertEquals(pa.getCity(), actual.getCity());
        assertEquals(pa.getVehicleCount(), actual.getVehicleCount());
    }

    @Test
    public void shouldThrowExceptionIfParkingAreaNotExistsWhenFindById(){
        assertThrows(ParkingAreaNotFound.class, () -> parkingAreaService.findById(2));
    }

    @Test
    public void shouldReturnParkingAreaIfParkingAreaExistsWhenFindById(){
        ParkingArea actual = parkingAreaService.findById(1);

        assertEquals(pa.getName(), actual.getName());
        assertEquals(pa.getCapacity(), actual.getCapacity());
        assertEquals(pa.getCity(), actual.getCity());
        assertEquals(pa.getVehicleCount(), actual.getVehicleCount());
    }

    @Test
    public void shouldReturnTotalIncomeWhenGetDailyIncome() throws ParseException {
        double actualIncome = parkingAreaService.getDailyIncome(1,"2020-11-20");
        assertEquals(130,actualIncome);

        actualIncome = parkingAreaService.getDailyIncome(2,"2020-11-20");
        assertEquals(25,actualIncome);
    }
}
