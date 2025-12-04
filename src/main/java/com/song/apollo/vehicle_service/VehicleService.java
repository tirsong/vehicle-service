package com.song.apollo.vehicle_service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle v) {
        if (vehicleRepository.existsById(v.getVin())) {
            throw new IllegalArgumentException("VIN already exists");
        } else {
            return vehicleRepository.save(v);
        }
    }

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

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleByVin(String vin) {
        return vehicleRepository.findById(vin);
    }

    public void deleteVehicle(String vin) {
        vehicleRepository.deleteById(vin);
    }
}
