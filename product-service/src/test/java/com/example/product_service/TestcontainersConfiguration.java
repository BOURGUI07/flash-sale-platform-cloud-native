package com.example.product_service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
		PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.12"))
				.withDatabaseName("product_db")
				.withUsername("user")
				.withPassword("password");
		postgresContainer.start();
		return postgresContainer;
	}

	@Bean
	@Primary
	ConnectionFactory connectionFactory(PostgreSQLContainer<?> postgresContainer) {
		ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
				.option(ConnectionFactoryOptions.DRIVER, "postgresql")
				.option(ConnectionFactoryOptions.HOST, postgresContainer.getHost())
				.option(ConnectionFactoryOptions.PORT, postgresContainer.getMappedPort(5432))
				.option(ConnectionFactoryOptions.DATABASE, postgresContainer.getDatabaseName())
				.option(ConnectionFactoryOptions.USER, postgresContainer.getUsername())
				.option(ConnectionFactoryOptions.PASSWORD, postgresContainer.getPassword())
				.build();
		return ConnectionFactories.get(options);
	}

	@Bean
	@Primary
	R2dbcTransactionManager transactionManager(ConnectionFactory connectionFactory) {
		return new R2dbcTransactionManager(connectionFactory);
	}

	@Bean
	DataSource dataSource(PostgreSQLContainer<?> postgresContainer) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(postgresContainer.getJdbcUrl());
		config.setUsername(postgresContainer.getUsername());
		config.setPassword(postgresContainer.getPassword());
		config.setDriverClassName(postgresContainer.getDriverClassName());
		return new HikariDataSource(config);
	}

	@Bean
	@Qualifier("jdbcTransactionManager")
	PlatformTransactionManager jdbcTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	CommandLineRunner initDatabase(DataSource dataSource) {
		return args -> {
			try (Connection conn = dataSource.getConnection()) {
				// Initialize your database schema
				ScriptUtils.executeSqlScript(conn, new FileSystemResource("src/main/resources/db/migration/V1__Initial_schema.sql"));
			}
		};
	}
}
