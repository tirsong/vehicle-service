package com.song.apollo.vehicle_service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for the business logic of Vehicle management.
 * Acts as an intermediary between the Controller and the Repository, handling
 * validation, data manipulation, and transaction flow.
 */
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    /**
     * Constructs a new VehicleService with the required repository dependency.
     *
     * @param vehicleRepository the repository used for data access.
     */
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Creates and persists a new vehicle in the system.
     * <p>
     * Checks if a vehicle with the given VIN already exists before saving.
     *
     * @param v the vehicle entity to be created.
     * @return the saved vehicle entity.
     * @throws IllegalArgumentException if a vehicle with the provided VIN already exists.
     */
    public Vehicle createVehicle(Vehicle v) {
        if (vehicleRepository.existsById(v.getVin())) {
            throw new IllegalArgumentException("VIN already exists");
        } else {
            return vehicleRepository.save(v);
        }
    }

    /**
     * Updates an existing vehicle by VIN.
     *
     * @param vin target vehicle VIN.
     * @param v   updated vehicle data.
     * @return updated vehicle or empty if not found.
     */
    public Optional<Vehicle> updateVehicle(String vin, Vehicle v) {
        return vehicleRepository.findById(vin).map(existingVehicle ->
        {
            existingVehicle.setManufacturerName(v.getManufacturerName());
            existingVehicle.setDescription(v.getDescription());
            existingVehicle.setHorsePower(v.getHorsePower());
            existingVehicle.setModelName(v.getModelName());
            existingVehicle.setPurchasePrice(v.getPurchasePrice());
            existingVehicle.setFuelType(v.getFuelType());
            return vehicleRepository.save(existingVehicle);
        });
    }

    /**
     * Retrieves a complete list of all vehicles currently stored in the database.
     *
     * @return a {@link List} of all {@link Vehicle} entities.
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Retrieves a specific vehicle by its unique VIN.
     *
     * @param vin the Vehicle Identification Number to search for.
     * @return an {@link Optional} containing the found vehicle, or empty if no vehicle matches the VIN.
     */
    public Optional<Vehicle> getVehicleByVin(String vin) {
        return vehicleRepository.findById(vin);
    }

    /**
     * Permanently removes a vehicle from the system.
     *
     * @param vin the Vehicle Identification Number of the vehicle to delete.
     */
    public void deleteVehicle(String vin) {
        vehicleRepository.deleteById(vin);
    }
}
