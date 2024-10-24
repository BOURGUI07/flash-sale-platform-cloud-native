package com.example.customer_service;

import com.example.customer_service.repo.OrderRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureWebTestClient
public abstract class AbstractTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    protected WebTestClient client;

    @BeforeEach
    void setup() throws SQLException {
        cleanup(); // Ensure clean state before each test
    }

    @AfterEach
    void cleanup() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String deleteOrderHistoryQuery = "DELETE FROM order_history";
            try (PreparedStatement stmt = conn.prepareStatement(deleteOrderHistoryQuery)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Failed to clean order_history table: " + e.getMessage());
                throw e;
            }
        }
    }

}
