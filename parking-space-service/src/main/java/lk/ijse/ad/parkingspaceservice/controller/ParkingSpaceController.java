package lk.ijse.ad.parkingspaceservice.controller;

import lk.ijse.ad.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.ad.parkingspaceservice.repository.ParkingSpaceRepository;
import lk.ijse.ad.parkingspaceservice.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceRepository repository;

    @Autowired
    private PricingService pricingService;

    @PostMapping
    public ResponseEntity<ParkingSpace> create(@RequestBody ParkingSpace space) {
        return ResponseEntity.ok(repository.save(space));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpace>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ParkingSpace> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ParkingSpace space = repository.findById(id).orElseThrow();
        space.setStatus(status);
        return ResponseEntity.ok(repository.save(space));
    }

    @PutMapping("/{id}/reserve")
    public ResponseEntity<?> reserve(@PathVariable Long id) {
        try {
            ParkingSpace space = repository.findById(id).orElseThrow();
            if (!space.getStatus().equals("AVAILABLE")) {
                return ResponseEntity.status(409).body("Space is not available");
            }
            space.setStatus("RESERVED");
            repository.save(space);
            double price = pricingService.calculatePrice(space.getBasePrice());
            return ResponseEntity.ok(Map.of("space", space, "calculatedPrice", price));
        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e) {
            return ResponseEntity.status(409).body("Reservation conflict — try again.");
        }
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<?> getCurrentPrice(@PathVariable Long id) {
        ParkingSpace space = repository.findById(id).orElseThrow();
        double currentPrice = pricingService.calculatePrice(space.getBasePrice());
        return ResponseEntity.ok(Map.of(
                "spaceId", space.getId(),
                "basePrice", space.getBasePrice(),
                "currentPrice", currentPrice,
                "isPeakHour", currentPrice > space.getBasePrice()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}