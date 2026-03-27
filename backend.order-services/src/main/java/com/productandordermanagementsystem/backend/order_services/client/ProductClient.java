package com.productandordermanagementsystem.backend.order_services.client;

import com.productandordermanagementsystem.backend.order_services.dto.product.InventoryDeductionRequest;
import com.productandordermanagementsystem.backend.order_services.dto.product.ProductSnapshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductClient {

    private final WebClient.Builder webClientBuilder;
    private final String productBaseUrl;

    public ProductClient(WebClient.Builder webClientBuilder, @Value("${services.product-base-url}") String productBaseUrl) {
        this.webClientBuilder = webClientBuilder;
        this.productBaseUrl = productBaseUrl;
    }

    public ProductSnapshot getProduct(Long productId, String authHeader) {
        return webClientBuilder.build()
                .get()
                .uri(productBaseUrl + "/api/products/" + productId)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(ProductSnapshot.class)
                .block();
    }

    public void deductInventory(Long productId, Integer quantity, String authHeader) {
        webClientBuilder.build()
                .post()
                .uri(productBaseUrl + "/internal/products/deduct")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new InventoryDeductionRequest(productId, quantity))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
