package lk.ijse.ad.parkingspaceservice.repository;

import lk.ijse.ad.parkingspaceservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    List<ParkingSpace> findByLocation(String location);
    List<ParkingSpace> findByStatus(String status);
}