package com.springcloud.mscv.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception{
        return http.authorizeExchange( authz -> {
            authz.pathMatchers("/authorized", "/logout").permitAll()
            .pathMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
            .pathMatchers(HttpMethod.GET, "/api/v1/products/{id}").hasAnyRole("ADMIN", "USER")
            .pathMatchers("/api/v1/products/**").hasRole("ADMIN")
            .anyExchange().authenticated();
        }).cors(csrf -> csrf.disable())
        .oauth2Login(withDefaults())
        .oauth2Client(withDefaults())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
        .build();
        
    }
    
}
