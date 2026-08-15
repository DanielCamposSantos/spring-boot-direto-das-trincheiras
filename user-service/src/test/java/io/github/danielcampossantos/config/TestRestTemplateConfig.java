package io.github.danielcampossantos.config;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static io.github.danielcampossantos.commons.Constants.BASE_URL;

@TestConfiguration
@Lazy
public class TestRestTemplateConfig {
    @LocalServerPort
    private int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        var uri = new DefaultUriBuilderFactory(BASE_URL + port);
        var testRestTemplate = new TestRestTemplate()
                .withBasicAuth("tester@email.com", "test");
        testRestTemplate.setUriTemplateHandler(uri);
        return testRestTemplate;

    }

}