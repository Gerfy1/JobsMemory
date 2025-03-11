package com.Jobs.JobsMemory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/test")
public class PublicController {

    @GetMapping("/hello")
    public String hello() {
        return "API funcionando! Acesso direto OK.";
    }

    @GetMapping("/db-status")
    public String dbStatus(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            return "Conexão com banco de dados OK: " + conn.getMetaData().getDatabaseProductName();
        } catch (Exception e) {
            return "Erro DB: " + e.getMessage();
        }
    }
}
