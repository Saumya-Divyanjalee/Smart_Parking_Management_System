package lk.ijse.ad.parkingspaceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_space")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private String zone;
    private String status;
    private String ownerId;
    private Double basePrice;

    @Version
    private Long version;
}