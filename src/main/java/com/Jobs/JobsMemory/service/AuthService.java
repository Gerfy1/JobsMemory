package com.Jobs.JobsMemory.service;

import com.Jobs.JobsMemory.model.LoginResponse;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.repository.UserRepository;
import com.Jobs.JobsMemory.util.JwtUtil;
import java.util.ArrayList;
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
        Optional<User> foundUser = this.userRepository.findByUsername(user.getUsername());
        if (foundUser.isPresent() && this.passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            User authenticatedUser = foundUser.get(); // Extract once
            String token = this.jwtUtil.generateToken(
                    new org.springframework.security.core.userdetails.User(
                            authenticatedUser.getUsername(),
                            authenticatedUser.getPassword(),
                            new ArrayList<>()
                    )
            );
            return ResponseEntity.ok(new LoginResponse(token, authenticatedUser.getId(), authenticatedUser.getUsername()));
        } else {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }
    }

    public ResponseEntity<?> register(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return ResponseEntity.status(201).body("Usuário registrado com sucesso");
    }

    public Optional<User> findByUsername(String username) {
        System.out.println("Searching for user: " + username);
        Optional<User> user = this.userRepository.findByUsername(username);
        System.out.println("User found: " + user.isPresent());
        return user;
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
