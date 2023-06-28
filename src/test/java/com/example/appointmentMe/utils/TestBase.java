package com.example.appointmentMe.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicBoolean;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {TestBase.PsqlInitializer.class})
public class TestBase {
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.7"))
            .withDatabaseName("appointment_me_db")
            .withUsername("postgres")
            .withPassword(StringUtils.EMPTY);

    public static class PsqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            if (!STARTED.get()) {
                STARTED.set(true);
                TestPropertyValues.of(
                        "spring.datasource.url=" + POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                        "spring.datasource.username=" + POSTGRE_SQL_CONTAINER.getUsername(),
                        "spring.datasource.password=" + POSTGRE_SQL_CONTAINER.getPassword()
                ).applyTo(applicationContext);
                POSTGRE_SQL_CONTAINER.start();
            } else {
                STARTED.set(false);
                POSTGRE_SQL_CONTAINER.stop();
            }
        }
    }

}
