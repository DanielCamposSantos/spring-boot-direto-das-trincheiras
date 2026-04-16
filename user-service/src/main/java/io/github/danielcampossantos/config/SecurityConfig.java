package io.github.danielcampossantos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;


@Configuration
public class SecurityConfig {
    private static final String[] WHITE_LIST = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/csrf"};
    private static final String PATH = "/v1/users";


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(new CookieCsrfTokenRepository())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                )
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(HttpMethod.GET, PATH).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, PATH).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PATH).hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }


}
