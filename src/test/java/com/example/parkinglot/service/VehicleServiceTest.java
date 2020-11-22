package com.example.parkinglot.service;

import com.example.parkinglot.entity.Type;
import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.repository.VehicleRepository;
import com.example.parkinglot.service.impl.VehicleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class VehicleServiceTest {

    @InjectMocks
    VehicleService vehicleService;

    @Mock
    VehicleRepository vehicleRepository;

    @Before
    public void setup() {
        Vehicle vehicle = new Vehicle(1, "34M01", Type.MINIVAN);
        Vehicle savedVehicle = new Vehicle(1, "34KLM01", Type.SEDAN);
        Mockito.when(vehicleRepository.findByPlate("34M01")).thenReturn(java.util.Optional.of(vehicle));
        Mockito.when(vehicleRepository.save(savedVehicle)).thenReturn(savedVehicle);
        Mockito.when(vehicleRepository.findById(1)).thenReturn(java.util.Optional.of(vehicle));
    }

    @Test
    public void shouldThrowExceptionIfVehicleTypeInvalidWhenCreateVehicle(){
        Vehicle v = new Vehicle(1, "34M01", null);
        assertThrows(RuntimeException.class, () -> vehicleService.createVehicle(v));
    }

    @Test
    public void shouldReturnNullIfVehicleExistsWhenCreateVehicle(){
        Vehicle v = new Vehicle(1, "34M01", Type.MINIVAN);
        assertNull(vehicleService.createVehicle(v));
    }

    @Test
    public void shouldReturnVehicleIfVehicleNotExistsWhenCreateVehicle(){
        Vehicle v = new Vehicle(1, "34KLM01", Type.SEDAN);
        Vehicle actual =  vehicleService.createVehicle(v);
        assertEquals(v.getType(),  actual.getType());
        assertEquals(v.getPlate(),  actual.getPlate());
    }

    @Test
    public void shouldReturnVehicleIfVehicleFoundById(){
        Vehicle v = new Vehicle(1, "34M01", Type.MINIVAN);
        Optional<Vehicle> actual =  vehicleService.findById(1);
        if(actual.isPresent()) {
            assertEquals(v.getType(), actual.get().getType());
            assertEquals(v.getPlate(), actual.get().getPlate());
        }
    }

    @Test
    public void shouldReturnVehicleIfVehicleFoundByPlate(){
        Vehicle v = new Vehicle(1, "34M01", Type.MINIVAN);
        Optional<Vehicle> actual =  vehicleService.findByPlate("34M01");
        if(actual.isPresent()) {
            assertEquals(v.getType(), actual.get().getType());
            assertEquals(v.getPlate(), actual.get().getPlate());
        }
    }

}
