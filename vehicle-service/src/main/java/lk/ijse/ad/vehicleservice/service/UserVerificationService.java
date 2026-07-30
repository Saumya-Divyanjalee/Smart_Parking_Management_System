package lk.ijse.ad.vehicleservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserVerificationService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "fallbackVerifyUser")
    public String verifyUser(String userId) {
        return webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/user/" + userId)
                .retrieve().bodyToMono(String.class).block();
    }

    public String fallbackVerifyUser(String userId, Throwable throwable) {
        return "User verification unavailable — proceeding with unverified registration.";
    }
}