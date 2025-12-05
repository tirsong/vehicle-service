package com.song.apollo.vehicle_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Vehicle} persistence.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations (save, findById, findAll, deleteById)
 * without needing boilerplate code. Spring Data JPA generates the implementation at runtime.
 * <p>
 * The ID type is {@link String} because the Vehicle entity uses the VIN as its primary key.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
