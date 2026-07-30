package lk.ijse.ad.paymentservice.controller;

import lk.ijse.ad.paymentservice.entity.Transaction;
import lk.ijse.ad.paymentservice.repository.TransactionRepository;
import lk.ijse.ad.paymentservice.util.PayHereUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private PayHereUtil payHereUtil;

    @Value("${payhere.merchant-id}")
    private String merchantId;

    // Mock payment (original flow)
    @PostMapping("/process")
    public ResponseEntity<Transaction> processPayment(@RequestBody Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
            transaction.setStatus("FAILED");
        } else {
            transaction.setStatus("SUCCESS");
            transaction.setReceiptId("RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        transaction.setTransactionDate(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(transaction));
    }

    // PayHere — generate hash & checkout payload
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody Transaction transaction) {
        String orderId = "ORDER-" + System.currentTimeMillis();
        String amount = String.format("%.2f", transaction.getAmount());
        String currency = "LKR";
        String hash = payHereUtil.generateHash(merchantId, orderId, amount, currency);

        return ResponseEntity.ok(Map.of(
                "merchant_id", merchantId,
                "order_id", orderId,
                "amount", amount,
                "currency", currency,
                "hash", hash
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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