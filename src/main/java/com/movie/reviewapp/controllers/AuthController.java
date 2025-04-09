package com.movie.reviewapp.controllers;

import com.movie.reviewapp.models.User;
import com.movie.reviewapp.repositories.UserRepository;
import com.movie.reviewapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User found = userRepository.findByEmail(user.getEmail());
        if (found != null && passwordEncoder.matches(user.getPassword(), found.getPassword())) {
            String token = jwtUtil.generateToken(found.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, found));
        }
        return ResponseEntity.status(401).build();
    }

    public record AuthResponse(String token, User user) {}
}
