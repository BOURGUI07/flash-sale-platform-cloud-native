package com.example.customer_service;

import com.example.customer_service.repo.OrderRepo;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class AbstractTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    protected WebTestClient client;

    @AfterEach
    void cleanup() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            // Delete all customers except for Alice, Bob, and Charlie
            String deleteQuery = "DELETE FROM customer WHERE name NOT IN ('Alice', 'Bob', 'Charlie')";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }


                // Delete all order history records
            String deleteOrderHistoryQuery = "DELETE FROM order_history";
            try (PreparedStatement stmt = conn.prepareStatement(deleteOrderHistoryQuery)) {
                stmt.executeUpdate();
            }

        }
    }
}
