package com.song.apollo.vehicle_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.*;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createVehicle_ShouldReturn201_WhenValid() throws Exception {
        Vehicle v = Vehicle.builder()
                .vin("V123")
                .manufacturerName("Ford")
                .description("Desc")
                .horsePower(100)
                .modelName("Fiesta")
                .purchasePrice(BigDecimal.valueOf(20000))
                .fuelType(Vehicle.FuelType.GASOLINE)
                .build();

        when(vehicleService.createVehicle(any())).thenReturn(v);

        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isCreated());
    }

    @Test
    void createVehicle_ShouldReturn422_WhenPriceIsNegative() throws Exception {
        Vehicle v = Vehicle.builder()
                .vin("V123")
                .manufacturerName("Ford")
                .description("Desc")
                .horsePower(100)
                .modelName("Fiesta")
                .fuelType(Vehicle.FuelType.GASOLINE)
                .purchasePrice(BigDecimal.valueOf(-100))
                .build();
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteVehicle_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/vehicle/V123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createVehicle_ShouldReturnBadRequest_WhenJsonIsMalformed() throws Exception {
        String malformedJson = """
            {
                "vin": "VIN123",
                "manufacturerName": "Tesla"
            """;
        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void createVehicle_ShouldReturnConflict_WhenVinAlreadyExists() throws Exception {
        Vehicle requestVehicle = Vehicle.builder()
                .vin("DUPLICATE-VIN")
                .manufacturerName("Ford")
                .modelName("Mustang")
                .description("Muscle Car")
                .horsePower(450)
                .purchasePrice(new BigDecimal("40000.00"))
                .fuelType(Vehicle.FuelType.GASOLINE)
                .build();

        when(vehicleService.createVehicle(any(Vehicle.class)))
                .thenThrow(new IllegalArgumentException("VIN already exists"));

        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestVehicle)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("VIN already exists"));
    }

    @Test
    void getAllVehicles_ShouldReturnList() throws Exception {
        Vehicle v1 = Vehicle.builder().vin("V1").manufacturerName("Ford").build();
        Vehicle v2 = Vehicle.builder().vin("V2").manufacturerName("Tesla").build();

        when(vehicleService.getAllVehicles()).thenReturn(Arrays.asList(v1, v2));

        mockMvc.perform(get("/vehicle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].vin").value("V1"))
                .andExpect(jsonPath("$[1].manufacturerName").value("Tesla"));
    }

    @Test
    void getVehicleById_ShouldReturn200_WhenFound() throws Exception {
        Vehicle v = Vehicle.builder().vin("V123").manufacturerName("Ford").build();

        when(vehicleService.getVehicleByVin("V123")).thenReturn(Optional.of(v));

        mockMvc.perform(get("/vehicle/V123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("V123"));
    }

    @Test
    void getVehicleById_ShouldReturn404_WhenNotFound() throws Exception {
        when(vehicleService.getVehicleByVin("UNKNOWN")).thenReturn(Optional.empty());

        mockMvc.perform(get("/vehicle/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateVehicle_ShouldReturn200_WhenUpdated() throws Exception {
        Vehicle updateInfo = Vehicle.builder()
                .vin("V123")
                .manufacturerName("NewName")
                .description("NewDesc")
                .horsePower(200)
                .modelName("NewModel")
                .purchasePrice(BigDecimal.valueOf(30000))
                .fuelType(Vehicle.FuelType.ELECTRIC)
                .build();

        when(vehicleService.updateVehicle(eq("V123"), any(Vehicle.class)))
                .thenReturn(Optional.of(updateInfo));

        mockMvc.perform(put("/vehicle/V123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturerName").value("NewName"));
    }

    @Test
    void updateVehicle_ShouldReturn404_WhenNotFound() throws Exception {
        Vehicle updateInfo = Vehicle.builder()
                .vin("UNKNOWN")
                .manufacturerName("NewName")
                .description("Desc")
                .horsePower(100)
                .modelName("Model")
                .purchasePrice(BigDecimal.valueOf(100))
                .fuelType(Vehicle.FuelType.GASOLINE)
                .build();

        when(vehicleService.updateVehicle(eq("UNKNOWN"), any(Vehicle.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/vehicle/UNKNOWN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isNotFound());
    }


}
