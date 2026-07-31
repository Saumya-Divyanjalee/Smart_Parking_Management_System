package lk.ijse.ad.parkingspaceservice.service;

import org.springframework.stereotype.Service;
import java.time.LocalTime;

@Service
public class PricingService {
    public double calculatePrice(Double basePrice) {
        if (basePrice == null) {
            return 0.0; // හෝ default price එකක් දෙන්න
        }
        LocalTime now = LocalTime.now();
        boolean isPeakHour =
                (now.isAfter(LocalTime.of(7, 0)) && now.isBefore(LocalTime.of(9, 0))) ||
                        (now.isAfter(LocalTime.of(17, 0)) && now.isBefore(LocalTime.of(19, 0)));
        return isPeakHour ? basePrice * 1.5 : basePrice;
    }
}