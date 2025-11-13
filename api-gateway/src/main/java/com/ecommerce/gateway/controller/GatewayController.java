package com.ecommerce.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final DiscoveryClient discoveryClient;

    public GatewayController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/info")
    public Mono<Map<String, Object>> getGatewayInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", applicationName);
        info.put("status", "UP");
        info.put("services", getRegisteredServices());
        info.put("routes", getConfiguredRoutes());
        return Mono.just(info);
    }

    @GetMapping("/services")
    public Mono<List<String>> getRegisteredServices() {
        return Mono.just(discoveryClient.getServices());
    }

    private List<String> getConfiguredRoutes() {
        return List.of(
                "/api/products/**",
                "/api/orders/**",
                "/api/inventory/**",
                "/api/notifications/**"
        );
    }
}