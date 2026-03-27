package com.productandordermanagementsystem.backend.order_services.client;

import com.productandordermanagementsystem.backend.order_services.dto.user.UserSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserClient {

    private final WebClient.Builder webClientBuilder;
    private final String userBaseUrl;

    public UserClient(WebClient.Builder webClientBuilder, @Value("${services.user-base-url}") String userBaseUrl) {
        this.webClientBuilder = webClientBuilder;
        this.userBaseUrl = userBaseUrl;
    }

    public UserSummary getUserSummary(Long userId, String authHeader) {
        return webClientBuilder.build()
                .get()
                .uri(userBaseUrl + "/api/users/" + userId)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(UserSummary.class)
                .block();
    }
}
