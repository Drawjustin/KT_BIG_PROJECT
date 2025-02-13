package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class DatabaseConnectionTest {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-test")
    public String testConnection() {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
            return "데이터베이스 연결 성공!";
        } catch (SQLException e) {
            return "데이터베이스 연결 실패: " + e.getMessage();
        }
    }
}