package ru.clevertec.ecl.service;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class AbstractIntegrationTest {

    protected static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>("postgres:15.2")
                .withPassword("password")
                .withUsername("username")
                .withDatabaseName("test")
                .withInitScript("sql/init.sql");
        container.start();
    }


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }
}
