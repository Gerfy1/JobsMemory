package com.Jobs.JobsMemory.service;

import com.Jobs.JobsMemory.model.LoginResponse;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.repository.UserRepository;
import com.Jobs.JobsMemory.util.JwtUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthService() {
    }

    public ResponseEntity<?> login(User user) {
        System.out.println("=== INÍCIO DO PROCESSO DE LOGIN ===");
        System.out.println("Tentativa de login para: " + user.getUsername());

        try {
            Optional<User> foundUser = this.userRepository.findByUsername(user.getUsername());

            if (foundUser.isPresent()) {
                User dbUser = foundUser.get();
                System.out.println("Usuário encontrado no DB: " + dbUser.getUsername());
                System.out.println("Senha enviada (primeiros 10 chars): " +
                        (user.getPassword().length() > 10 ? user.getPassword().substring(0, 10) + "..." : user.getPassword()));
                System.out.println("Senha no DB (primeiros 10 chars): " + dbUser.getPassword().substring(0, 10) + "...");

                boolean passwordMatches = this.passwordEncoder.matches(user.getPassword(), dbUser.getPassword());
                System.out.println("Senhas coincidem? " + passwordMatches);

                if (passwordMatches) {
                    System.out.println("Autenticação bem-sucedida, gerando token...");
                    String token = this.jwtUtil.generateToken(
                            new org.springframework.security.core.userdetails.User(
                                    dbUser.getUsername(),
                                    dbUser.getPassword(),
                                    new ArrayList<>()
                            )
                    );
                    System.out.println("Token gerado com sucesso");
                    return ResponseEntity.ok(new LoginResponse(token, dbUser.getId(), dbUser.getUsername()));
                } else {
                    System.out.println("Senha incorreta");
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Usuário ou senha inválidos");
                    return ResponseEntity.status(401).body(response);
                }
            } else {
                System.out.println("Usuário não encontrado: " + user.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Usuário ou senha inválidos");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            System.out.println("ERRO no login: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Erro no servidor: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    public ResponseEntity<?> register(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return ResponseEntity.status(201).body("Usuário registrado com sucesso");
    }

    public Optional<User> findByUsername(String username) {
        try {
            System.out.println("Searching for username in database: " + username);
            Optional<User> result = this.userRepository.findByUsername(username);
            System.out.println("Result found: " + result.isPresent());
            if (result.isPresent()) {
                System.out.println("User details: " + result.get().getId() + " - " + result.get().getUsername());
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error in findByUsername: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
