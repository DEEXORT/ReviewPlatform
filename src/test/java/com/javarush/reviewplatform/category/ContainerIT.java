package com.javarush.reviewplatform.category;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainerIT {
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    static {

    }
}
