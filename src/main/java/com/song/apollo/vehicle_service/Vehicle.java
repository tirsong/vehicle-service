package com.song.apollo.vehicle_service;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Domain entity representing a Vehicle.
 * <p>
 * This class maps to the "vehicles" table in the database and includes
 * validation constraints that are enforced during creation and updates.
 * <p>
 * Uses Lombok annotations to automatically generate getters, setters,
 * constructors, and builder patterns at compile time.
 */
@Entity
@Table(name = "vehicles")
@Data
@Builder
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
    private FuelType fuelType;

    public enum FuelType {
        GASOLINE,
        DIESEL,
        ELECTRIC,
        HYBRID
    }

}
