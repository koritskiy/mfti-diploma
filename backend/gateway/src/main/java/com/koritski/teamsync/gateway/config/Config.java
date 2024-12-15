package com.koritski.teamsync.gateway.config;

import com.koritski.teamsync.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Autowired
    private JwtAuthenticationFilter authFilter;

    /**
     * Конфигурация роутов и параметры для фильтрации запросов.
     * Каждый запрос раскидывается по микросервисам в зависимости от префикса в url
     * @param builder роут-builder
     * @return кастомный роут-распределитель
     */
    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("tracker",
                        route -> route.path("/api/tracker/**")
                                .filters(filter -> filter.filter(authFilter))
                                .uri("lb://tracker"))
                .route("auth",
                        route -> route.path("/api/auth/**")
                                .filters(filter -> filter.filter(authFilter))
                                .uri("lb://auth"))
                .route("hiring",
                        route -> route.path("/api/hiring/**")
                                .filters(filter -> filter.filter(authFilter))
                                .uri("lb://hiring"))
                .build();
    }
}
