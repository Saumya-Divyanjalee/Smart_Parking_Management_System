package lk.ijse.ad.paymentservice.controller;

import java.util.HashMap;
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

    @Value("${payhere.return-url}")
    private String returnUrl;

    @Value("${payhere.cancel-url}")
    private String cancelUrl;

    @Value("${payhere.notify-url}")
    private String notifyUrl;

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


    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody Transaction transaction) {
        String orderId = "ORDER-" + System.currentTimeMillis();
        String amount = String.format("%.2f", transaction.getAmount());
        String currency = "LKR";
        String hash = payHereUtil.generateHash(merchantId, orderId, amount, currency);

        transaction.setStatus("PENDING");
        transaction.setTransactionDate(LocalDateTime.now());
        Transaction saved = repository.save(transaction);

        Map<String, Object> response = new HashMap<>();
        response.put("merchant_id", merchantId);
        response.put("order_id", orderId);
        response.put("items", "Parking Reservation");
        response.put("amount", amount);
        response.put("currency", currency);
        response.put("hash", hash);
        response.put("first_name", "Customer");
        response.put("last_name", "SPMS");
        response.put("email", "customer@spms.lk");
        response.put("phone", "0771234567");
        response.put("address", "Colombo");
        response.put("city", "Colombo");
        response.put("country", "Sri Lanka");
        response.put("return_url", returnUrl);
        response.put("cancel_url", cancelUrl);
        response.put("notify_url", notifyUrl);
        response.put("transaction_id", saved.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok("Payment successful! Order: " + params.get("order_id"));
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok("Payment cancelled. Order: " + params.get("order_id"));
    }

    @PostMapping("/notify")
    public ResponseEntity<String> paymentNotify(@RequestParam Map<String, String> params) {
        String notifiedMerchantId = params.get("merchant_id"); // renamed — field එකට collision වෙන්නේ නෑ
        String orderId = params.get("order_id");
        String payhereAmount = params.get("payhere_amount");
        String payhereCurrency = params.get("payhere_currency");
        String statusCode = params.get("status_code");
        String md5sig = params.get("md5sig");

        boolean isValid = payHereUtil.verifyNotification(notifiedMerchantId, orderId, payhereAmount, payhereCurrency, statusCode, md5sig);

        if (isValid && "2".equals(statusCode)) {
            return ResponseEntity.ok("Notification processed successfully");
        }
        return ResponseEntity.status(400).body("Invalid notification");
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