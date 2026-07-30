package lk.ijse.ad.parkingspaceservice.controller;

import lk.ijse.ad.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.ad.parkingspaceservice.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceRepository repository;

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
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ParkingSpace> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ParkingSpace space = repository.findById(id).orElseThrow();
        space.setStatus(status);
        return ResponseEntity.ok(repository.save(space));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}