package lk.ijse.ad.userservice.controller;

import lk.ijse.ad.userservice.dto.UserResponseDTO;
import lk.ijse.ad.userservice.entity.User;
import lk.ijse.ad.userservice.repository.UserRepository;
import lk.ijse.ad.userservice.service.EmailService;
import lk.ijse.ad.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = repository.save(user);
        emailService.sendWelcomeEmail(saved.getEmail(), saved.getName());
        return ResponseEntity.ok(toDTO(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = repository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            return ResponseEntity.ok(Map.of("token", token, "user", toDTO(user)));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(repository.findAll().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return repository.findById(id).map(u -> ResponseEntity.ok(toDTO(u))).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody User updated) {
        User user = repository.findById(id).orElseThrow();
        user.setName(updated.getName());
        user.setEmail(updated.getEmail());
        return ResponseEntity.ok(toDTO(repository.save(user)));
    }

    private UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}