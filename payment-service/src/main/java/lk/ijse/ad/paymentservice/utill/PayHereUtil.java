package lk.ijse.ad.paymentservice.utill;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;

@Component
public class PayHereUtil {
    @Value("${payhere.merchant-secret}")
    private String merchantSecret;

    public String generateHash(String merchantId, String orderId, String amount, String currency) {
        try {
            String hashedSecret = md5(merchantSecret).toUpperCase();
            String rawString = merchantId + orderId + amount + currency + hashedSecret;
            return md5(rawString).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Hash generation failed", e);
        }
    }

    private String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}