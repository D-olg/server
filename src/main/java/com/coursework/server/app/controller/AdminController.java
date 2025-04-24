package com.coursework.server.app.controller;

import com.coursework.server.app.model.User;
import com.coursework.server.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("Запрос на получение всех пользователей для админа...");
        try {
            List<User> users = userService.getAllUsers();
            System.out.println("Получены пользователи: " + users.size() + " записей.");
            return ResponseEntity.ok(users); // Возвращаем список пользователей
        } catch (Exception e) {
            System.out.println("Ошибка при получении пользователей: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // Возвращаем ошибку сервера
        }
    }
}
