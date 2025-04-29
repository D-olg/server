package com.coursework.server.app.controller;

import com.coursework.server.app.dto.*;
import com.coursework.server.app.model.User;
import com.coursework.server.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService service, PasswordEncoder passwordEncoder) {
        this.userService = service;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if (userService.userExists(registerDTO.getEmail())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword())); // ХЭШИРОВАНИЕ
        user.setEmail(registerDTO.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(0); // Обычный пользователь

        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.findByUsername(loginDTO.getUsername());

        if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            // Обновляем время последнего входа и статус
            user.setLastLogin(LocalDateTime.now());
            user.setOnlineStatus(true);
            userService.saveUser(user); // сохраняем изменения

            String role = (user.getRole() == 1) ? "admin" : "user";
            return ResponseEntity.ok("Login successful. Role: " + role);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }


    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Server is working");
    }

    @GetMapping("/admin-test")
    public ResponseEntity<String> adminTest(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user != null && user.getRole() == 1) {
            return ResponseEntity.ok("Hello Admin: " + user.getUsername());
        }
        return ResponseEntity.status(403).body("Access denied: Admins only");
    }
}
