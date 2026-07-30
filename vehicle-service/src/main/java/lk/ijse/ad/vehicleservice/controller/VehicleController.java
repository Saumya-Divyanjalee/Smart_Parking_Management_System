package lk.ijse.ad.vehicleservice.controller;

import lk.ijse.ad.vehicleservice.entity.Vehicle;
import lk.ijse.ad.vehicleservice.repository.VehicleRepository;
import lk.ijse.ad.vehicleservice.service.UserVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleRepository repository;

    @Autowired
    private UserVerificationService userVerificationService;

    @PostMapping
    public ResponseEntity<Vehicle> register(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(repository.save(vehicle));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/entry")
    public ResponseEntity<Vehicle> markEntry(@PathVariable Long id) {
        Vehicle v = repository.findById(id).orElseThrow();
        v.setEntryStatus("ENTERED");
        return ResponseEntity.ok(repository.save(v));
    }

    @PutMapping("/{id}/exit")
    public ResponseEntity<Vehicle> markExit(@PathVariable Long id) {
        Vehicle v = repository.findById(id).orElseThrow();
        v.setEntryStatus("EXITED");
        return ResponseEntity.ok(repository.save(v));
    }

    @GetMapping("/verify-user/{userId}")
    public ResponseEntity<String> verifyUser(@PathVariable String userId) {
        return ResponseEntity.ok(userVerificationService.verifyUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}