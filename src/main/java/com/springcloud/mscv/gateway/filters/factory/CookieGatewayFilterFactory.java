package com.springcloud.mscv.gateway.filters.factory;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

// all filter factorry personalized class has to have the postfix GatewayFilterFactory 
// example name + GatewayFilterFactory
@Component
public class CookieGatewayFilterFactory
        extends AbstractGatewayFilterFactory<CookieGatewayFilterFactory.ConfigurationCookie> {

    private final Logger log = LoggerFactory.getLogger(CookieGatewayFilterFactory.class);

    public CookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        return (exchange, chain) -> {
            log.info("Executing pre gateway filter factory" + config.message);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.ofNullable(config.value).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.name, cookie).build());
                });
                log.info("Executing post gateway filter factory" + config.message);
            }));
        };
    }

    @Getter
    @Setter
    public static class ConfigurationCookie {
        private String name;
        private String value;
        private String message;
    }
}
