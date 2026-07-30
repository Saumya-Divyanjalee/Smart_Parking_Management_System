package lk.ijse.ad.paymentservice.controller;

import lk.ijse.ad.paymentservice.entity.Transaction;
import lk.ijse.ad.paymentservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private TransactionRepository repository;

    @PostMapping("/process")
    public ResponseEntity<Transaction> processPayment(@RequestBody Transaction transaction) {
        // Mock validation — card number 16 digits check කරනවා විදියට simulate කරන්න
        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
            transaction.setStatus("FAILED");
        } else {
            transaction.setStatus("SUCCESS");
            transaction.setReceiptId("RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        transaction.setTransactionDate(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(repository.findByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}