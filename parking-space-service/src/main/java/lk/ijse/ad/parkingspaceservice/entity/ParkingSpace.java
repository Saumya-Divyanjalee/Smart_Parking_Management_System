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
    private String status; // AVAILABLE, OCCUPIED, RESERVED
    private String ownerId;

    @Version
    private Long version; // optimistic locking සඳහා (Step 9 එකට useful)
}