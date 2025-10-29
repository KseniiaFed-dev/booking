package com.example.booking_service.controller;

import com.example.booking_service.model.UserEntity;
import com.example.booking_service.service.UserService;
import com.example.booking_service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // Получить всех пользователей (для теста)
    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Получить пользователя по username
    @GetMapping("/{username}")
    public ResponseEntity<Optional<UserEntity>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // Сохранить нового пользователя
    @PostMapping("/save")
    public ResponseEntity<UserEntity> saveUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    // Удалить пользователя
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 🔑 Аутентификация: получение JWT
    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody UserEntity user) {
        Optional<UserEntity> existingUser = userService.getUserByUsername(user.getUsername());

        if (existingUser.isEmpty() ||
                !existingUser.get().getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtTokenProvider.generateToken(
                existingUser.get().getUsername(),
                existingUser.get().getRole()
        );

        return ResponseEntity.ok(Map.of("token", token));
    }
}