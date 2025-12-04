package com.song.apollo.vehicle_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

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

        mockMvc.perform(post("/vehicle"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(v))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createVehicle_ShouldReturn422_WhenPriceIsNegative() throws Exception {
        Vehicle v = Vehicle.builder()
                .vin("V123")
                .purchasePrice(BigDecimal.valueOf(-100))
                .build();
        mockMvc.perform(post("/vehicle"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(v))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteVehicle_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/vehicle/V123"))
                .andExpect(status().isNoContent());\
    }


}
