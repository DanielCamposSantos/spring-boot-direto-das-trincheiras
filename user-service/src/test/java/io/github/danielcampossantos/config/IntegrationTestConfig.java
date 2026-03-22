package io.github.danielcampossantos.config;

import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class IntegrationTestConfig {
}
