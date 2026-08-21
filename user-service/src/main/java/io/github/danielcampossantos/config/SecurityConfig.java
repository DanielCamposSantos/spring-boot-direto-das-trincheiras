package io.github.danielcampossantos.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

  private static final String[] WHITE_LIST = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/csrf"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITE_LIST).permitAll()
            .requestMatchers(HttpMethod.GET, getPath()).hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.POST, getPath()).permitAll()
            .requestMatchers(HttpMethod.DELETE, getPath()).hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.PUT, getPath()).permitAll()
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  private static @NonNull String getPath() {
    return "/v1/users";
  }

}
