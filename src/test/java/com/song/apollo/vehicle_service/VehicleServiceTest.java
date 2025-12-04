package com.song.apollo.vehicle_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicle_ShouldSave_WhenVinIsUnique(){
        Vehicle v = Vehicle.builder()
                .vin("TEST_VIN")
                .purchasePrice(BigDecimal.TEN)
                .build();
        when(vehicleRepository.existsById("TEST_VIN")).thenReturn(false);
        when(vehicleRepository.save(v)).thenReturn(v);

        Vehicle result = vehicleService.createVehicle(v);

        assertNotNull(result);
        verify(vehicleRepository).save(v);
    }

    @Test
    void createVehicle_ShouldThrowException_WhenVinExists(){
        Vehicle v = Vehicle.builder().vin("DUPLICATE_VIN").build();
        when(vehicleRepository.existsById("DUPLICATE_VIN")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> vehicleService.createVehicle(v));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void updateVehicle_ShouldUpdate_WhenVehicleExists(){
        String vin = "EXISTING_VIN";
        Vehicle oldVehicle = Vehicle.builder().vin(vin).manufacturerName("OldName").build();
        Vehicle newDetails = Vehicle.builder().manufacturerName("NewName").build();

        when(vehicleRepository.findById(vin)).thenReturn(Optional.of(oldVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Vehicle> result = vehicleService.updateVehicle(vin, newDetails);
        assertTrue(result.isPresent());
        assertEquals("NewName", result.get().getManufacturerName());
    }
}
