package lk.ijse.ad.vehicleservice.repository;

import lk.ijse.ad.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByPlateNumber(String plateNumber);
}