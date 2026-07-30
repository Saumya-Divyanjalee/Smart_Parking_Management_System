package lk.ijse.ad.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Double amount;
    private String status; // SUCCESS, FAILED, PENDING
    private String cardLastFourDigits; // mock card - only last 4 digits store කරන්න
    private String receiptId;
    private LocalDateTime transactionDate;
}