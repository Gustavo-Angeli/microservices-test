package com.gusta.bank.integrationtests.testcontainers;

import org.springframework.context.*;
import org.springframework.core.env.*;
import org.springframework.test.context.*;
import org.testcontainers.containers.*;
import org.testcontainers.lifecycle.*;

import java.util.*;
import java.util.stream.*;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.29");

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource
                    ("testContainers",(Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testContainers);
        }

    }
}
