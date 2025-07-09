package com.springcloud.mscv.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GlobalGatewayFilter implements GlobalFilter, Ordered {

    private final Logger log = LoggerFactory.getLogger(GlobalGatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("executing request filter");

        var mutateRequest = exchange.getRequest().mutate().header("token", "ert-23").build();
        exchange.mutate().request(mutateRequest).build();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("executing response for filter");

            Optional.ofNullable(exchange.getRequest().getHeaders().get("token")).ifPresent(value -> {
                log.info("token" + value.getFirst());
                exchange.getResponse().getHeaders().add("token", value.getFirst());
            });

            Optional.ofNullable(exchange.getRequest().getHeaders().get("wave")).ifPresent(value -> {
                log.info("wave" + value.getFirst());
                exchange.getResponse().getHeaders().add("wave", value.getFirst());
            });

            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "Red").build());
            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
            exchange.getResponse().getStatusCode().isError();
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
