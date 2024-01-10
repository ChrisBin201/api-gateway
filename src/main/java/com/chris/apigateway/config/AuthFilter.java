package com.chris.apigateway.config;

import com.chris.apigateway.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public static class Config {
        // empty class as I don't need any particular configuration
    }

    private AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("running");
//            if(!exchange.getRequest().getHeaders().containsKey("X-auth-user-id")) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization information");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                String[] parts = authHeader.split(" ");

                if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                    throw new RuntimeException("Incorrect authorization structure");
                }
//            return webClientBuilder.build()
//                    .post()
////                    .uri("http://localhost:8080/api/auth/validateToken?token=" + parts[1])
//                    .uri("https://jsqedvglu56wklwlbxfhvc2ude.srv.us/api/auth/validateToken?token=" + parts[1])
//                    .retrieve().bodyToMono(UserDto.class)
//                    .map(userDto -> {
//                        exchange.getRequest()
//                                .mutate()
//                                .header("X-auth-user-id", String.valueOf(userDto.getId()))
//                                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ userDto.getToken());
//                        log.info(userDto.getId()+" "+userDto.getUsername() +" " +userDto.getToken());
//                        return exchange;
//                    }).flatMap(chain::filter);
////            }
            return chain.filter(exchange) ;
        };
    }

}
