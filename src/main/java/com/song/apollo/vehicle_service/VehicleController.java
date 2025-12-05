package com.song.apollo.vehicle_service;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Vehicle resources.
 * <p>
 * This class exposes endpoints for CRUD operations (Create, Read, Update, Delete)
 * on vehicles. It handles incoming HTTP requests, invokes the business logic
 * via {@link VehicleService}, and returns appropriate HTTP responses.
 * <p>
 * Base URL: /vehicle
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    /**
     * Constructor Injection for Service Layer.
     *
     * @param vehicleService the business logical service instance.
     */
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * GET /vehicle
     * Retrieves all vehicles.
     *
     * @return List of vehicles (200 OK).
     */
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    /**
     * POST /vehicle
     * Creates a new vehicle.
     *
     * @param vehicle Request body (validated).
     * @return Created vehicle (201 Created).
     * @throws IllegalArgumentException if VIN exists.
     */
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleService.createVehicle(vehicle);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    /**
     * GET /vehicle/{vin}
     * Retrieves a vehicle by VIN.
     *
     * @return Vehicle (200 OK) or 404 Not Found.
     */
    @GetMapping("/{vin}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String vin) {
        return vehicleService.getVehicleByVin(vin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /vehicle/{vin}
     * Updates an existing vehicle.
     *
     * @return Updated vehicle (200 OK) or 404 Not Found.
     */
    @PutMapping("/{vin}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String vin, @Valid @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(vin, vehicle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /vehicle/{vin}
     * Deletes a vehicle by VIN.
     *
     * @return 204 No Content (Strict requirement).
     */
    @DeleteMapping("/{vin}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String vin) {
        vehicleService.deleteVehicle(vin);
        return ResponseEntity.noContent().build();
    }


}
