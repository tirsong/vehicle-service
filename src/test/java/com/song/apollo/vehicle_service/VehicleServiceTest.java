package com.song.apollo.vehicle_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @InjectMocks
    private VehicleService vehicleService;
    private Vehicle sampleVehicle;

    @BeforeEach
    void setUp() {
        sampleVehicle = Vehicle.builder()
                .vin("TEST-VIN-100")
                .manufacturerName("Toyota")
                .modelName("Camry")
                .description("Sedan")
                .horsePower(203)
                .purchasePrice(new BigDecimal("25000.00"))
                .fuelType(Vehicle.FuelType.GASOLINE)
                .build();
    }

    @Test
    void createVehicle_ShouldSave_WhenVinIsUnique() {
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
    void createVehicle_ShouldThrowException_WhenVinExists() {
        Vehicle v = Vehicle.builder().vin("DUPLICATE_VIN").build();
        when(vehicleRepository.existsById("DUPLICATE_VIN")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> vehicleService.createVehicle(v));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void updateVehicle_ShouldUpdate_WhenVehicleExists() {
        String vin = "EXISTING_VIN";
        Vehicle oldVehicle = Vehicle.builder().vin(vin).manufacturerName("OldName").build();
        Vehicle newDetails = Vehicle.builder().manufacturerName("NewName").build();

        when(vehicleRepository.findById(vin)).thenReturn(Optional.of(oldVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Vehicle> result = vehicleService.updateVehicle(vin, newDetails);
        assertTrue(result.isPresent());
        assertEquals("NewName", result.get().getManufacturerName());
    }

    @Test
    void getAllVehicles_ShouldReturnListOfVehicles() {
        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(sampleVehicle));

        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        assertThat(vehicles).hasSize(1);
        assertThat(vehicles.get(0).getVin()).isEqualTo("TEST-VIN-100");
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void getVehicleByVin_ShouldReturnVehicle_WhenFound() {
        when(vehicleRepository.findById("TEST-VIN-100")).thenReturn(Optional.of(sampleVehicle));

        Optional<Vehicle> result = vehicleService.getVehicleByVin("TEST-VIN-100");

        assertThat(result).isPresent();
        assertThat(result.get().getManufacturerName()).isEqualTo("Toyota");
        verify(vehicleRepository, times(1)).findById("TEST-VIN-100");
    }

    @Test
    void getVehicleByVin_ShouldReturnEmpty_WhenNotFound() {
        when(vehicleRepository.findById("UNKNOWN-VIN")).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleService.getVehicleByVin("UNKNOWN-VIN");

        assertThat(result).isEmpty();
        verify(vehicleRepository, times(1)).findById("UNKNOWN-VIN");
    }

    @Test
    void deleteVehicle_ShouldCallRepositoryDelete() {
        String vinToDelete = "TEST-VIN-100";
        vehicleService.deleteVehicle(vinToDelete);

        verify(vehicleRepository, times(1)).deleteById(vinToDelete);
    }


}
