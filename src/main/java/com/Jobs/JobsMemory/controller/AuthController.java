package com.Jobs.JobsMemory.controller;

import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.repository.UserRepository;
import com.Jobs.JobsMemory.service.AuthService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/api/auth"})
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://login-angular-eight.vercel.app",
                "https://login-angular-git-master-gerfy1s-projects.vercel.app",
                "https://login-angular-n7ak3ydtd-gerfy1s-projects.vercel.app"
        },
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class AuthController {
    @Autowired
    private AuthService authService;



    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController() {
    }

    @PostMapping({"/register"})
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        try {
            logger.info("Received register request: {}", user);
            boolean userExists = false;
            try {
                userExists = this.userRepository.existsByUsername(user.getUsername());
                logger.info("User exists check result: {}", userExists);
            } catch (Exception e) {
                logger.error("Error checking if user exists", e);
                e.printStackTrace();
            }

            if (!userExists) {
                logger.info("Creating new user: {}", user.getUsername());
                this.authService.register(user);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Usuário criado com sucesso");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                logger.info("User already exists: {}", user.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Esse usuário já existe");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Error in registration process", e);
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
    @GetMapping("/test-auth")
    @ResponseBody
    public String testAuth() {
        try {
            long userCount = userRepository.count();

            StringBuilder result = new StringBuilder();
            result.append("Conexão com banco OK.\n");
            result.append("Usuários encontrados: ").append(userCount).append("\n");

            if (userCount == 0) {
                User testUser = new User();
                testUser.setUsername("test" + System.currentTimeMillis());
                testUser.setPassword(passwordEncoder.encode("password"));
                testUser.setRole("USER");

                User saved = userRepository.save(testUser);
                result.append("Usuário de teste criado com ID: ").append(saved.getId());
            } else {
                result.append("Usuários existentes:\n");
                userRepository.findAll().forEach(u -> result.append("- ").append(u.getUsername()).append("\n"));
            }

            return result.toString();
        } catch (Exception e) {
            return "ERRO: " + e.getMessage() + "\n" + e.getClass().getName();
        }
    }
}
