package com.Jobs.JobsMemory.controller;

import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.service.AuthService;
import java.util.HashMap;
import java.util.Map;
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
        origins = {"https://login-angular-eight.vercel.app/login"}
)
public class AuthController {
    @Autowired
    private AuthService authService;

    public AuthController() {
    }

    @PostMapping({"/register"})
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        System.out.println("Received register request: " + user);
        if (this.authService.findByUsername(user.getUsername()) != null) {
            Map<String, String> response = new HashMap();
            response.put("message", "Esse usuário já existe");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            this.authService.register(user);
            Map<String, String> response = new HashMap();
            response.put("message", "Usuário criado com sucesso");
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
    }

    @PostMapping({"/login"})
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        System.out.println("Received login request: " + user);
        User existUser = (User)this.authService.findByUsername(user.getUsername()).orElse(null);
        if (existUser != null && this.authService.checkPassword(user.getPassword(), existUser.getPassword())) {
            Map<String, Object> response = new HashMap();
            response.put("message", "Login efetuado com sucesso");
            response.put("userId", existUser.getId());
            response.put("username", existUser.getUsername());
            response.put("token", this.authService.login(user).getBody());
            return new ResponseEntity(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap();
            response.put("message", "Usuário ou senha inválidos");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
}
