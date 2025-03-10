package com.Jobs.JobsMemory.controller;

import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.service.AuthService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/api/auth"})
@CrossOrigin(
        origins = {"https://login-angular-eight.vercel.app"}
)
public class AuthController {
    @Autowired
    private AuthService authService;

    public AuthController() {
    }

    @PostMapping({"/register"})
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        try {
            System.out.println("Received register request: " + user);

            System.out.println("Searching for username: " + user.getUsername());

            Optional<User> existingUser = this.authService.findByUsername(user.getUsername());
            System.out.println("Result of findByUsername: " + existingUser);

            if (existingUser.isPresent()) {
                System.out.println("User already exists: " + user.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Esse usuário já existe");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                System.out.println("Creating new user: " + user.getUsername());
                this.authService.register(user);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Usuário criado com sucesso");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Erro no processamento do registro: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/login"})
    public ResponseEntity<?> login(@RequestBody User user) {
        System.out.println("Received login request: " + user);
        return this.authService.login(user);
    }
}
