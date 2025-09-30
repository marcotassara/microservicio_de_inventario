package com.proyecto.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClientProductos(WebClient.Builder builder) {
        // Creamos un "teléfono" pre-configurado para llamar al servicio de productos
        return builder.baseUrl("http://localhost:8082/api/v1/productos").build();
    }
}