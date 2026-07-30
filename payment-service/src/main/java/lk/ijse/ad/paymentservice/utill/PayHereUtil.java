package lk.ijse.ad.paymentservice.util;

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

    // PayHere notify_url එකෙන් එන data ම verify කරන්න (security — spoofed requests block කරන්න)
    public boolean verifyNotification(String merchantId, String orderId, String payhereAmount,
                                      String payhereCurrency, String statusCode, String md5sig) {
        try {
            String hashedSecret = md5(merchantSecret).toUpperCase();
            String rawString = merchantId + orderId + payhereAmount + payhereCurrency + statusCode + hashedSecret;
            String localMd5sig = md5(rawString).toUpperCase();
            return localMd5sig.equals(md5sig);
        } catch (Exception e) {
            return false;
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