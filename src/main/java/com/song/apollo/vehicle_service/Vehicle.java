package com.song.apollo.vehicle_service;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @NotBlank(message = "VIN is required")
    private String vin;

    @NotBlank(message = "Manufacturer name is required")
    private String manufacturerName;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Horsepower must be greater than 0")
    private Integer horsePower;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal purchasePrice;

    @NotNull(message = "Fuel type is required")
    @Enumerated(EnumType.STRING)
    String fuelType;

    public enum FuelType {
        GASOLINE,
        DIESEL,
        ELECTRIC,
        HYBRID
    }

}
