package com.salesforcechina.gateway;


import com.salesforcechina.varton.api.apiservice.Test1Service;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@Slf4j
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> {

    @DubboReference(check = false, lazy = true)
    private Test1Service test1Service;
    @Value("${service.tag}")
    private String serviceTag;

    private Mono<Void> filterBusinessApi(ServerWebExchange exchange, GatewayFilterChain chain, Config config) {
        String token = test1Service.isUserAvailable("1");
        log.info(String.format("dubbo call %s", token));
        String headerValue = String.format("gateway[%s]", serviceTag);
        ServerHttpRequest request = exchange.getRequest().mutate().header("GATEWAY" ,headerValue).build();
        return chain.filter(exchange.mutate().request(request).build());
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String host = exchange.getRequest().getURI().getHost();
            return filterBusinessApi(exchange, chain, config);
        });
    }

    @Getter
    @Setter
    protected static class Config {
        private List<String> businessApiNoCheck = new ArrayList<>();

        private List<String> openApiNoCheck = new ArrayList<>();

    }
}
